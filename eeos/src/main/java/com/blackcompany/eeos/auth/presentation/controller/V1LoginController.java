package com.blackcompany.eeos.auth.presentation.controller;

import com.blackcompany.eeos.auth.application.domain.TokenModel;
import com.blackcompany.eeos.auth.application.dto.converter.TokenResponseConverter;
import com.blackcompany.eeos.auth.application.dto.request.EEOSLoginRequest;
import com.blackcompany.eeos.auth.application.dto.response.TokenResponse;
import com.blackcompany.eeos.auth.application.usecase.LoginUsecase;
import com.blackcompany.eeos.auth.presentation.docs.V1AuthApi;
import com.blackcompany.eeos.auth.presentation.support.AuthConstants;
import com.blackcompany.eeos.auth.presentation.support.AuthCookieManager;
import com.blackcompany.eeos.common.presentation.response.ApiResponse;
import com.blackcompany.eeos.common.presentation.response.ApiResponseBody.SuccessBody;
import com.blackcompany.eeos.common.presentation.response.ApiResponseGenerator;
import com.blackcompany.eeos.common.presentation.response.MessageCode;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class V1LoginController implements V1AuthApi {

	private final LoginUsecase loginUsecase;
	private final AuthCookieManager cookieManager;
	private final TokenResponseConverter tokenResponseConverter;

	@Override
	@PostMapping("/login")
	public ApiResponse<SuccessBody<TokenResponse>> login(
			@RequestHeader(value = "Client-Type", defaultValue = "WEB") String clientType,
			@RequestBody EEOSLoginRequest request,
			HttpServletResponse httpResponse) {

		TokenModel tokenModel = loginUsecase.login(request.getId(), request.getPassword());

		if ("APP".equalsIgnoreCase(clientType)) {
			// APP: AT + RT 모두 body
			TokenResponse response =
					tokenResponseConverter.from(
							tokenModel.getAccessToken(),
							tokenModel.getAccessExpiredTime(),
							tokenModel.getRefreshToken());
			return ApiResponseGenerator.success(response, HttpStatus.CREATED, MessageCode.CREATE);
		}

		// WEB (기본): RT 쿠키 + AT body
		TokenResponse response =
				tokenResponseConverter.from(tokenModel.getAccessToken(), tokenModel.getAccessExpiredTime());
		ResponseCookie cookie =
				cookieManager.setCookie(AuthConstants.TOKEN_KEY, tokenModel.getRefreshToken());
		httpResponse.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

		return ApiResponseGenerator.success(response, HttpStatus.CREATED, MessageCode.CREATE);
	}
}
