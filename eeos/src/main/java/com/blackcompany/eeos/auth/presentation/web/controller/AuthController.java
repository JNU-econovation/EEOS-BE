package com.blackcompany.eeos.auth.presentation.web.controller;

import com.blackcompany.eeos.auth.application.domain.TokenModel;
import com.blackcompany.eeos.auth.application.dto.converter.TokenResponseConverter;
import com.blackcompany.eeos.auth.application.dto.response.TokenResponse;
import com.blackcompany.eeos.auth.application.usecase.LoginUsecase;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponse;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseBody.SuccessBody;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseGenerator;
import com.blackcompany.eeos.common.presentation.respnose.MessageCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final LoginUsecase loginUsecase;
	private final TokenResponseConverter tokenResponseConverter;

	@Value("${api.domain}")
	private String domain;

	@PostMapping("/login/{oauthServerType}")
	ApiResponse<SuccessBody<TokenResponse>> login(
			@PathVariable String oauthServerType, @RequestParam("code") String code) {
		TokenModel tokenModel = loginUsecase.login(oauthServerType, code);
		TokenResponse response =
				tokenResponseConverter.from(tokenModel.getAccessToken(), tokenModel.getAccessExpiredTime());

		return ApiResponseGenerator.success(
				response, HttpStatus.CREATED, MessageCode.CREATE, setCookieValue(tokenModel));
	}

	private String setCookieValue(TokenModel model) {
		return String.format(
				"%s = %s; %s = %s; %s = %s; ",
				"token",
				model.getRefreshToken(),
				"Max-Age",
				model.getRefreshExpiredTime(),
				"Domain",
				domain);
	}
}
