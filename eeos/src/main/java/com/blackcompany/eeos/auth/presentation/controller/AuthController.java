package com.blackcompany.eeos.auth.presentation.controller;

import com.blackcompany.eeos.auth.application.domain.TokenModel;
import com.blackcompany.eeos.auth.application.dto.converter.TokenResponseConverter;
import com.blackcompany.eeos.auth.application.dto.request.EEOSLoginRequest;
import com.blackcompany.eeos.auth.application.dto.response.TokenResponse;
import com.blackcompany.eeos.auth.application.usecase.LogOutUsecase;
import com.blackcompany.eeos.auth.application.usecase.LoginUsecase;
import com.blackcompany.eeos.auth.application.usecase.ReissueUsecase;
import com.blackcompany.eeos.auth.application.usecase.WithDrawUsecase;
import com.blackcompany.eeos.auth.presentation.docs.AuthApi;
import com.blackcompany.eeos.auth.presentation.support.AuthConstants;
import com.blackcompany.eeos.auth.presentation.support.Member;
import com.blackcompany.eeos.auth.presentation.support.TokenExtractor;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponse;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseBody.SuccessBody;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseGenerator;
import com.blackcompany.eeos.common.presentation.respnose.MessageCode;
import com.blackcompany.eeos.common.presentation.support.CookieManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

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

	public AuthController(
			LoginUsecase loginUsecase,
			ReissueUsecase reissueUsecase,
			@Qualifier("cookie") TokenExtractor tokenExtractor,
			TokenResponseConverter tokenResponseConverter,
			CookieManager cookieManager,
			LogOutUsecase logOutUsecase,
			WithDrawUsecase withDrawUsecase) {
		this.loginUsecase = loginUsecase;
		this.reissueUsecase = reissueUsecase;
		this.tokenExtractor = tokenExtractor;
		this.tokenResponseConverter = tokenResponseConverter;
		this.cookieManager = cookieManager;
		this.logOutUsecase = logOutUsecase;
		this.withDrawUsecase = withDrawUsecase;
	}

	@Override
	@PostMapping("/login/{oauthServerType}")
	public ApiResponse<SuccessBody<TokenResponse>> login(
			@PathVariable String oauthServerType,
			@RequestParam("code") String code,
			@RequestParam("redirect_uri") String uri,
			HttpServletResponse httpResponse) {
		TokenModel tokenModel = loginUsecase.login(oauthServerType, code, uri);
		TokenResponse response = generateTokenResponse(tokenModel, httpResponse);

		return ApiResponseGenerator.success(response, HttpStatus.CREATED, MessageCode.CREATE);
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
