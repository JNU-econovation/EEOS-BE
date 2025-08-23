package com.blackcompany.eeos.auth.presentation.controller;

import com.blackcompany.eeos.auth.application.domain.TokenModel;
import com.blackcompany.eeos.auth.application.dto.converter.TokenResponseConverter;
import com.blackcompany.eeos.auth.application.dto.request.AdditionalInfoApplicationCommand;
import com.blackcompany.eeos.auth.application.dto.request.AuthorityUpdateRequest;
import com.blackcompany.eeos.auth.application.dto.request.EEOSLoginRequest;
import com.blackcompany.eeos.auth.application.dto.request.OAuthLoginRequestCommand;
import com.blackcompany.eeos.auth.application.dto.response.RoleResponse;
import com.blackcompany.eeos.auth.application.dto.response.TokenResponse;
import com.blackcompany.eeos.auth.application.model.Role;
import com.blackcompany.eeos.auth.application.usecase.*;
import com.blackcompany.eeos.auth.presentation.docs.AuthApi;
import com.blackcompany.eeos.auth.presentation.dto.AdditionalInfoRequest;
import com.blackcompany.eeos.auth.presentation.support.AuthConstants;
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
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
	private final TokenResponseConverter tokenResponseConverter;
	private final LogOutUsecase logOutUsecase;
	private final WithDrawUsecase withDrawUsecase;
	private final OAuthSignUpUseCase oAuthSignUpUseCase;
	private final AuthorityUsecase authorityUsecase;

	public AuthController(
			LoginUsecase loginUsecase,
			ReissueUsecase reissueUsecase,
			@Qualifier("cookie") TokenExtractor tokenExtractor,
			TokenResponseConverter tokenResponseConverter,
			CookieManager cookieManager,
			LogOutUsecase logOutUsecase,
			WithDrawUsecase withDrawUsecase,
			OAuthSignUpUseCase oAuthSignUpUseCase,
			AuthorityUsecase authorityUsecase) {
		this.loginUsecase = loginUsecase;
		this.reissueUsecase = reissueUsecase;
		this.tokenExtractor = tokenExtractor;
		this.tokenResponseConverter = tokenResponseConverter;
		this.cookieManager = cookieManager;
		this.logOutUsecase = logOutUsecase;
		this.withDrawUsecase = withDrawUsecase;
		this.oAuthSignUpUseCase = oAuthSignUpUseCase;
		this.authorityUsecase = authorityUsecase;
	}

	@Override
	@PostMapping("/login/{oauthServerType}")
	public ApiResponse<SuccessBody<TokenResponse>> login(
			@PathVariable String oauthServerType,
			@RequestParam("code") String code,
			@RequestParam("redirect_uri") String uri,
			HttpServletResponse httpResponse) {
		String formatUri = uri.trim().replaceAll("[\n\r\t ]", "");

		OAuthLoginRequestCommand command =
				new OAuthLoginRequestCommand(oauthServerType, code, formatUri);
		TokenModel tokenModel = loginUsecase.login(command);

		TokenResponse response = generateTokenResponse(tokenModel, httpResponse);

		// 마이그레이션 필요 체크 - 임시 코드
		HttpHeaders headers = new HttpHeaders();
		if ("slack".equals(oauthServerType)) {
			headers.add("Migration-Required", "true");
		}

		return ApiResponseGenerator.success(response, HttpStatus.CREATED, headers, MessageCode.CREATE);
	}

	@Override
	@PostMapping("/login")
	public ApiResponse<SuccessBody<TokenResponse>> login(
			@RequestBody EEOSLoginRequest request, HttpServletResponse httpResponse) {
		TokenModel tokenModel = loginUsecase.login(request.getId(), request.getPassword());
		TokenResponse response = generateTokenResponse(tokenModel, httpResponse);
		return ApiResponseGenerator.success(response, HttpStatus.CREATED, MessageCode.CREATE);
	}

	@Override
	@PostMapping("/reissue")
	public ApiResponse<SuccessBody<TokenResponse>> reissue(
			HttpServletRequest request, HttpServletResponse httpResponse) {
		String token = tokenExtractor.extract(request);
		TokenModel tokenModel = reissueUsecase.execute(token);
		TokenResponse response = generateTokenResponse(tokenModel, httpResponse);

		return ApiResponseGenerator.success(response, HttpStatus.CREATED, MessageCode.CREATE);
	}

	@Override
	@PostMapping("/logout")
	public ApiResponse<SuccessBody<Void>> logout(
			HttpServletRequest request, HttpServletResponse httpResponse, @Member Long memberId) {
		String token = tokenExtractor.extract(request);
		logOutUsecase.logOut(token, memberId);
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

	@PutMapping("/authority")
	public ApiResponse<SuccessBody<Void>> updateAuthority(
			@RequestBody @Valid AuthorityUpdateRequest request){
		authorityUsecase.changeRole(request.memberId(), request.from(), request.to());
		return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.UPDATE);
	}

	@GetMapping("/authority")
	public ApiResponse<SuccessBody<List<RoleResponse>>> getAuthorities(){
		List<Role> roles = authorityUsecase.getOrganizationRoles();
		List<RoleResponse> responses = roles.stream().map(RoleResponse::from).toList();
		return ApiResponseGenerator.success(responses, HttpStatus.OK, MessageCode.UPDATE);
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

	private void deleteTokenResponse(HttpServletResponse httpServletResponse) {
		ResponseCookie cookie = cookieManager.deleteCookie(AuthConstants.TOKEN_KEY);
		httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
	}
}
