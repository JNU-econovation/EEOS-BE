package com.blackcompany.eeos.auth.presentation.interceptor;

import com.blackcompany.eeos.auth.application.domain.token.TokenValidator;
import com.blackcompany.eeos.auth.presentation.support.TokenExtractor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthInterceptor implements HandlerInterceptor {
	private final TokenExtractor tokenExtractor;
	private final TokenValidator tokenValidator;

	public static AuthInterceptorBuilder builder() {
		return new AuthInterceptorBuilder();
	}

	@Override
	public boolean preHandle(
			HttpServletRequest request, HttpServletResponse response, Object handler) {
		if (CorsUtils.isPreFlightRequest(request)) {
			return true;
		}

		canExtractToken(request);
		return true;
	}

	private void canExtractToken(HttpServletRequest request) {
		String token = tokenExtractor.extract(request);
		tokenValidator.valid(token);
	}

	public static class AuthInterceptorBuilder {

		private TokenExtractor tokenExtractor;
		private TokenValidator tokenValidator;

		public AuthInterceptorBuilder tokenExtractor(TokenExtractor tokenExtractor) {
			this.tokenExtractor = tokenExtractor;
			return this;
		}

		public AuthInterceptorBuilder tokenValidator(TokenValidator tokenValidator) {
			this.tokenValidator = tokenValidator;
			return this;
		}

		public AuthInterceptor build() {
			return new AuthInterceptor(tokenExtractor, tokenValidator);
		}
	}
}
