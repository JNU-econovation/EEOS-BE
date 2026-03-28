package com.blackcompany.eeos.auth.presentation.controller;

import com.blackcompany.eeos.auth.application.domain.ClientType;
import com.blackcompany.eeos.auth.application.domain.TokenModel;
import com.blackcompany.eeos.auth.application.exception.InvalidClientException;
import com.blackcompany.eeos.auth.application.exception.InvalidRedirectUriException;
import com.blackcompany.eeos.auth.application.service.ClientService;
import com.blackcompany.eeos.auth.application.service.OAuth2LoginService;
import com.blackcompany.eeos.auth.application.service.TokenExchangeService;
import com.blackcompany.eeos.auth.presentation.docs.OAuth2Api;
import com.blackcompany.eeos.auth.presentation.support.AuthCookieManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class OAuth2Controller implements OAuth2Api {

	private final ClientService clientService;
	private final OAuth2LoginService oAuth2LoginService;
	private final TokenExchangeService tokenExchangeService;
	private final AuthCookieManager cookieManager;

	@Value("${auth.login-page-url:http://localhost:3000/login}")
	private String loginPageUrl;

	@Override
	@GetMapping("/authorize")
	public ResponseEntity<Void> authorize(
			@RequestParam("client_id") String clientId,
			@RequestParam("redirect_uri") String redirectUri,
			@RequestParam("response_type") String responseType,
			@RequestParam("state") String state,
			@RequestParam(value = "code_challenge", required = false) String codeChallenge,
			@RequestParam(value = "code_challenge_method", required = false) String codeChallengeMethod) {

		if (!"code".equals(responseType)) {
			return ResponseEntity.badRequest().build();
		}

		var client = clientService.findAndValidateRedirectUri(clientId, redirectUri);

		if (client.getClientType() == ClientType.APP && codeChallenge == null) {
			return ResponseEntity.badRequest().build();
		}

		String location =
				UriComponentsBuilder.fromUriString(loginPageUrl)
						.queryParam("client_id", clientId)
						.queryParam("redirect_uri", redirectUri)
						.queryParam("state", state)
						.queryParamIfPresent("code_challenge", java.util.Optional.ofNullable(codeChallenge))
						.queryParamIfPresent(
								"code_challenge_method", java.util.Optional.ofNullable(codeChallengeMethod))
						.build()
						.toUriString();

		return ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION, location).build();
	}

	@Override
	@PostMapping("/login")
	public ResponseEntity<Void> login(
			@RequestParam("client_id") String clientId,
			@RequestParam("redirect_uri") String redirectUri,
			@RequestParam("state") String state,
			@RequestParam("email") String email,
			@RequestParam("password") String password,
			@RequestParam(value = "code_challenge", required = false) String codeChallenge,
			@RequestParam(value = "code_challenge_method", required = false) String codeChallengeMethod,
			HttpServletRequest request,
			HttpServletResponse response) {

		var client = validateClientOrBadRequest(clientId, redirectUri);
		if (client == null) {
			return ResponseEntity.badRequest().build();
		}

		String ip = request.getRemoteAddr();

		try {
			if (client.getClientType() == ClientType.WEB) {
				return handleWebLogin(clientId, redirectUri, state, email, password, ip, response);
			} else {
				return handleAppLogin(
						clientId, redirectUri, state, email, password, ip, codeChallenge, codeChallengeMethod);
			}
		} catch (Exception e) {
			return redirectToLoginPageWithError(
					clientId, redirectUri, state, codeChallenge, codeChallengeMethod);
		}
	}

	@Override
	@PostMapping(value = "/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<Map<String, Object>> token(
			@RequestParam("grant_type") String grantType,
			@RequestParam("code") String code,
			@RequestParam("code_verifier") String codeVerifier,
			@RequestParam("redirect_uri") String redirectUri,
			@RequestParam("client_id") String clientId) {

		if (!"authorization_code".equals(grantType)) {
			return ResponseEntity.badRequest().build();
		}

		TokenModel tokenModel =
				tokenExchangeService.exchange(code, codeVerifier, redirectUri, clientId);

		Map<String, Object> body = new HashMap<>();
		body.put("access_token", tokenModel.getAccessToken());
		body.put("refresh_token", tokenModel.getRefreshToken());
		body.put("token_type", "Bearer");
		body.put("expires_in", (tokenModel.getAccessExpiredTime() - System.currentTimeMillis()) / 1000);

		return ResponseEntity.ok(body);
	}

	private com.blackcompany.eeos.auth.persistence.client.ClientEntity validateClientOrBadRequest(
			String clientId, String redirectUri) {
		try {
			return clientService.findAndValidateRedirectUri(clientId, redirectUri);
		} catch (InvalidClientException | InvalidRedirectUriException e) {
			return null;
		}
	}

	private ResponseEntity<Void> handleWebLogin(
			String clientId,
			String redirectUri,
			String state,
			String email,
			String password,
			String ip,
			HttpServletResponse response) {
		TokenModel tokenModel =
				oAuth2LoginService.loginForWeb(clientId, redirectUri, email, password, ip);

		ResponseCookie atCookie = cookieManager.setAccessTokenCookie(tokenModel.getAccessToken());
		ResponseCookie rtCookie = cookieManager.setRefreshTokenCookie(tokenModel.getRefreshToken());
		response.addHeader(HttpHeaders.SET_COOKIE, atCookie.toString());
		response.addHeader(HttpHeaders.SET_COOKIE, rtCookie.toString());

		String location =
				UriComponentsBuilder.fromUriString(redirectUri)
						.queryParam("state", state)
						.build()
						.toUriString();

		return ResponseEntity.status(HttpStatus.SEE_OTHER)
				.header(HttpHeaders.LOCATION, location)
				.build();
	}

	private ResponseEntity<Void> handleAppLogin(
			String clientId,
			String redirectUri,
			String state,
			String email,
			String password,
			String ip,
			String codeChallenge,
			String codeChallengeMethod) {
		String code =
				oAuth2LoginService.loginForApp(
						clientId, redirectUri, email, password, ip, codeChallenge, codeChallengeMethod);

		String location =
				UriComponentsBuilder.fromUriString(redirectUri)
						.queryParam("code", code)
						.queryParam("state", state)
						.build()
						.toUriString();

		return ResponseEntity.status(HttpStatus.SEE_OTHER)
				.header(HttpHeaders.LOCATION, location)
				.build();
	}

	private ResponseEntity<Void> redirectToLoginPageWithError(
			String clientId,
			String redirectUri,
			String state,
			String codeChallenge,
			String codeChallengeMethod) {
		String location =
				UriComponentsBuilder.fromUriString(loginPageUrl)
						.queryParam("client_id", clientId)
						.queryParam("redirect_uri", redirectUri)
						.queryParam("state", state)
						.queryParamIfPresent("code_challenge", java.util.Optional.ofNullable(codeChallenge))
						.queryParamIfPresent(
								"code_challenge_method", java.util.Optional.ofNullable(codeChallengeMethod))
						.queryParam("error", "invalid_credentials")
						.build()
						.toUriString();

		return ResponseEntity.status(HttpStatus.SEE_OTHER)
				.header(HttpHeaders.LOCATION, location)
				.build();
	}
}
