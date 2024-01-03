package com.blackcompany.eeos.auth.presentation.web.controller;

import com.blackcompany.eeos.auth.application.domain.TokenModel;
import com.blackcompany.eeos.auth.application.dto.converter.TokenResponseConverter;
import com.blackcompany.eeos.auth.application.dto.response.TokenResponse;
import com.blackcompany.eeos.auth.application.usecase.LoginUsecase;
import com.blackcompany.eeos.auth.application.usecase.ReissueUsecase;
import com.blackcompany.eeos.auth.presentation.support.TokenExtractor;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponse;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseBody.SuccessBody;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseGenerator;
import com.blackcompany.eeos.common.presentation.respnose.MessageCode;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final LoginUsecase loginUsecase;
	private final ReissueUsecase reissueUsecase;
	private final TokenExtractor tokenExtractor;
	private final TokenResponseConverter tokenResponseConverter;

	private final String domain;

	public AuthController(
			LoginUsecase loginUsecase,
			ReissueUsecase reissueUsecase,
			@Qualifier("cookie") TokenExtractor tokenExtractor,
			TokenResponseConverter tokenResponseConverter,
			@Value("${api.domain}") String domain) {
		this.loginUsecase = loginUsecase;
		this.reissueUsecase = reissueUsecase;
		this.tokenExtractor = tokenExtractor;
		this.tokenResponseConverter = tokenResponseConverter;
		this.domain = domain;
	}

	@PostMapping("/login/{oauthServerType}")
	ApiResponse<SuccessBody<TokenResponse>> login(
			@PathVariable String oauthServerType, @RequestParam("code") String code) {
		TokenModel tokenModel = loginUsecase.login(oauthServerType, code);
		TokenResponse response =
				tokenResponseConverter.from(tokenModel.getAccessToken(), tokenModel.getAccessExpiredTime());

		return ApiResponseGenerator.success(
				response, HttpStatus.CREATED, MessageCode.CREATE, setCookieValue(tokenModel));
	}

	@PostMapping("/reissue")
	ApiResponse<SuccessBody<TokenResponse>> reissue(HttpServletRequest request) {
		String token = tokenExtractor.extract(request);
		TokenModel tokenModel = reissueUsecase.execute(token);
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
