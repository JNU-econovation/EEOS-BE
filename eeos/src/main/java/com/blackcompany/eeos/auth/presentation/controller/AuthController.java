package com.blackcompany.eeos.auth.presentation.controller;

import com.blackcompany.eeos.auth.application.domain.TokenModel;
import com.blackcompany.eeos.auth.application.domain.token.TokenResolver;
import com.blackcompany.eeos.auth.application.dto.converter.TokenResponseConverter;
import com.blackcompany.eeos.auth.application.dto.request.AdditionalInfoApplicationCommand;
import com.blackcompany.eeos.auth.application.dto.request.EeosSignUpCommand;
import com.blackcompany.eeos.auth.application.dto.response.TokenResponse;
import com.blackcompany.eeos.auth.application.usecase.EeosSignUpUseCase;
import com.blackcompany.eeos.auth.application.usecase.LogOutUsecase;
import com.blackcompany.eeos.auth.application.usecase.LoginUsecase;
import com.blackcompany.eeos.auth.application.usecase.OAuthSignUpUseCase;
import com.blackcompany.eeos.auth.application.usecase.ReissueUsecase;
import com.blackcompany.eeos.auth.application.usecase.WithDrawUsecase;
import com.blackcompany.eeos.auth.presentation.docs.AuthApi;
import com.blackcompany.eeos.auth.presentation.dto.AdditionalInfoRequest;
import com.blackcompany.eeos.auth.presentation.dto.EeosSignUpRequest;
import com.blackcompany.eeos.auth.presentation.support.AuthConstants;
import com.blackcompany.eeos.auth.presentation.support.AuthCookieManager;
import com.blackcompany.eeos.auth.presentation.support.Member;
import com.blackcompany.eeos.auth.presentation.support.TokenExtractor;
import com.blackcompany.eeos.auth.presentation.support.VerificationId;
import com.blackcompany.eeos.common.presentation.response.ApiResponse;
import com.blackcompany.eeos.common.presentation.response.ApiResponseBody.SuccessBody;
import com.blackcompany.eeos.common.presentation.response.ApiResponseGenerator;
import com.blackcompany.eeos.common.presentation.response.MessageCode;
import com.blackcompany.eeos.common.presentation.support.CookieManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController implements AuthApi {
	private final LoginUsecase loginUsecase;
	private final ReissueUsecase reissueUsecase;
	private final TokenExtractor tokenExtractor;
	private final CookieManager cookieManager;
	private final AuthCookieManager authCookieManager;
	private final TokenResponseConverter tokenResponseConverter;
	private final LogOutUsecase logOutUsecase;
	private final WithDrawUsecase withDrawUsecase;
	private final OAuthSignUpUseCase oAuthSignUpUseCase;
	private final EeosSignUpUseCase eeosSignUpUseCase;
	private final TokenResolver tokenResolver;

	public AuthController(
			LoginUsecase loginUsecase,
			ReissueUsecase reissueUsecase,
			@Qualifier("cookie") TokenExtractor tokenExtractor,
			TokenResponseConverter tokenResponseConverter,
			CookieManager cookieManager,
			AuthCookieManager authCookieManager,
			LogOutUsecase logOutUsecase,
			WithDrawUsecase withDrawUsecase,
			OAuthSignUpUseCase oAuthSignUpUseCase,
			EeosSignUpUseCase eeosSignUpUseCase,
			TokenResolver tokenResolver) {
		this.loginUsecase = loginUsecase;
		this.reissueUsecase = reissueUsecase;
		this.tokenExtractor = tokenExtractor;
		this.tokenResponseConverter = tokenResponseConverter;
		this.cookieManager = cookieManager;
		this.authCookieManager = authCookieManager;
		this.logOutUsecase = logOutUsecase;
		this.withDrawUsecase = withDrawUsecase;
		this.oAuthSignUpUseCase = oAuthSignUpUseCase;
		this.eeosSignUpUseCase = eeosSignUpUseCase;
		this.tokenResolver = tokenResolver;
	}

	@Override
	@PostMapping("/reissue")
	public ApiResponse<SuccessBody<TokenResponse>> reissue(
			HttpServletRequest request, HttpServletResponse httpResponse) {
		String token = tokenExtractor.extract(request);
		String clientType = tokenResolver.getClientTypeByRefreshToken(token);

		TokenModel tokenModel = reissueUsecase.execute(token);

		if ("WEB".equals(clientType)) {
			setWebCookies(tokenModel, httpResponse);
		}

		TokenResponse response =
				tokenResponseConverter.from(tokenModel.getAccessToken(), tokenModel.getAccessExpiredTime());
		return ApiResponseGenerator.success(response, HttpStatus.CREATED, MessageCode.CREATE);
	}

	@Override
	@PostMapping("/logout")
	public ApiResponse<SuccessBody<Void>> logout(
			HttpServletRequest request, HttpServletResponse httpResponse, @Member Long memberId) {
		String token = tokenExtractor.extract(request);
		String clientType = tokenResolver.getClientTypeByRefreshToken(token);

		logOutUsecase.logOut(token, memberId);

		if ("WEB".equals(clientType)) {
			deleteWebCookies(httpResponse);
		}
		deleteTokenResponse(httpResponse);

		return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.DELETE);
	}

	@Override
	@PostMapping("/withdraw")
	public ApiResponse<SuccessBody<Void>> withDraw(
			HttpServletRequest request, HttpServletResponse httpResponse, @Member Long memberId) {
		String token = tokenExtractor.extract(request);
		withDrawUsecase.withDraw(token, memberId);
		deleteTokenResponse(httpResponse);

		return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.DELETE);
	}

	@Override
	@PostMapping("/signup")
	public ApiResponse<SuccessBody<TokenResponse>> signUp(
			@Valid @RequestBody EeosSignUpRequest request,
			@RequestParam(required = false) String code,
			HttpServletResponse httpResponse) {
		EeosSignUpCommand command =
				new EeosSignUpCommand(
						request.getId(),
						request.getPassword(),
						request.getGeneration(),
						request.getName(),
						request.getActiveStatus());

		TokenModel tokenModel =
				Optional.ofNullable(code)
						.map(c -> eeosSignUpUseCase.signUp(command, c))
						.orElseGet(() -> eeosSignUpUseCase.signUp(command));

		TokenResponse response = generateTokenResponse(tokenModel, httpResponse);
		return ApiResponseGenerator.success(response, HttpStatus.CREATED, MessageCode.CREATE);
	}

	@PostMapping("/login/additional-info")
	public ApiResponse<SuccessBody<TokenResponse>> submitAdditionalInfo(
			@VerificationId UUID verificationId,
			@Valid @RequestBody AdditionalInfoRequest request,
			HttpServletResponse httpResponse) {
		TokenModel tokenModel =
				oAuthSignUpUseCase.signUp(
						new AdditionalInfoApplicationCommand(
								verificationId,
								request.getName(),
								request.getGeneration(),
								request.getActiveStatus()));
		TokenResponse response = generateTokenResponse(tokenModel, httpResponse);

		return ApiResponseGenerator.success(response, HttpStatus.CREATED, MessageCode.CREATE);
	}

	private TokenResponse generateTokenResponse(
			TokenModel tokenModel, HttpServletResponse httpServletResponse) {
		TokenResponse response =
				tokenResponseConverter.from(tokenModel.getAccessToken(), tokenModel.getAccessExpiredTime());

		ResponseCookie cookie =
				cookieManager.setCookie(AuthConstants.TOKEN_KEY, tokenModel.getRefreshToken());
		httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

		return response;
	}

	private void setWebCookies(TokenModel tokenModel, HttpServletResponse httpResponse) {
		ResponseCookie atCookie = authCookieManager.setAccessTokenCookie(tokenModel.getAccessToken());
		ResponseCookie rtCookie = authCookieManager.setRefreshTokenCookie(tokenModel.getRefreshToken());
		httpResponse.addHeader(HttpHeaders.SET_COOKIE, atCookie.toString());
		httpResponse.addHeader(HttpHeaders.SET_COOKIE, rtCookie.toString());
	}

	private void deleteWebCookies(HttpServletResponse httpResponse) {
		ResponseCookie atCookie = authCookieManager.deleteAccessTokenCookie();
		ResponseCookie rtCookie = authCookieManager.deleteRefreshTokenCookie();
		httpResponse.addHeader(HttpHeaders.SET_COOKIE, atCookie.toString());
		httpResponse.addHeader(HttpHeaders.SET_COOKIE, rtCookie.toString());
	}

	private void deleteTokenResponse(HttpServletResponse httpServletResponse) {
		ResponseCookie cookie = cookieManager.deleteCookie(AuthConstants.TOKEN_KEY);
		httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
	}
}
