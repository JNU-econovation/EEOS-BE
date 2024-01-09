package com.blackcompany.eeos.auth.presentation.controller;

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
import com.blackcompany.eeos.common.utils.TimeUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
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
	private final String cookieKey;
	private final String domain;

	public AuthController(
			LoginUsecase loginUsecase,
			ReissueUsecase reissueUsecase,
			@Qualifier("cookie") TokenExtractor tokenExtractor,
			TokenResponseConverter tokenResponseConverter,
			@Value("${api.cookie-key}") String cookieKey,
			@Value("${api.domain}") String domain) {
		this.loginUsecase = loginUsecase;
		this.reissueUsecase = reissueUsecase;
		this.tokenExtractor = tokenExtractor;
		this.tokenResponseConverter = tokenResponseConverter;
		this.cookieKey = cookieKey;
		this.domain = domain;
	}

	@PostMapping("/login/{oauthServerType}")
	ApiResponse<SuccessBody<TokenResponse>> login(
			@PathVariable String oauthServerType,
			@RequestParam("code") String code,
			@RequestParam("redirect_uri") String uri,
			HttpServletResponse httpResponse) {
		TokenModel tokenModel = loginUsecase.login(oauthServerType, code, uri);
		TokenResponse response = toResponse(tokenModel, httpResponse);

		return ApiResponseGenerator.success(response, HttpStatus.CREATED, MessageCode.CREATE);
	}

	@PostMapping("/reissue")
	ApiResponse<SuccessBody<TokenResponse>> reissue(
			HttpServletRequest request, HttpServletResponse httpResponse) {
		String token = tokenExtractor.extract(request);
		TokenModel tokenModel = reissueUsecase.execute(token);
		TokenResponse response = toResponse(tokenModel, httpResponse);

		return ApiResponseGenerator.success(response, HttpStatus.CREATED, MessageCode.CREATE);
	}

	private TokenResponse toResponse(TokenModel tokenModel, HttpServletResponse httpResponse) {
		TokenResponse response =
				tokenResponseConverter.from(tokenModel.getAccessToken(), tokenModel.getAccessExpiredTime());
		setCookie(httpResponse, tokenModel);

		return response;
	}

	private void setCookie(HttpServletResponse response, TokenModel tokenModel) {
		ResponseCookie cookie =
				ResponseCookie.from(cookieKey, tokenModel.getRefreshToken())
						.path("/")
						.domain(domain)
						.httpOnly(true)
						.secure(true)
						.maxAge(TimeUtil.convertSecondsFromMillis(tokenModel.getRefreshExpiredTime()))
						.build();

		response.addHeader("Set-Cookie", cookie.toString());
	}
}
