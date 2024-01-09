package com.blackcompany.eeos.auth.presentation.interceptor;

import com.blackcompany.eeos.auth.application.domain.token.TokenResolver;
import com.blackcompany.eeos.auth.presentation.support.TokenExtractor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthInterceptor implements HandlerInterceptor {
	private final TokenExtractor tokenExtractor;
	private final TokenResolver tokenResolver;

	public AuthInterceptor(
			@Qualifier("header") TokenExtractor tokenExtractor, TokenResolver tokenResolver) {
		this.tokenExtractor = tokenExtractor;
		this.tokenResolver = tokenResolver;
	}

	public static AuthInterceptorBuilder builder() {
		return new AuthInterceptorBuilder();
	}

	@Override
	public boolean preHandle(
			HttpServletRequest request, HttpServletResponse response, Object handler) {
		if (CorsUtils.isPreFlightRequest(request)) {
			return true;
		}

		String token = tokenExtractor.extract(request);
		tokenResolver.getUserInfoByHeader(token);
		return true;
	}

	public static class AuthInterceptorBuilder {

		private TokenExtractor tokenExtractor;
		private TokenResolver tokenResolver;

		public AuthInterceptorBuilder tokenExtractor(TokenExtractor tokenExtractor) {
			this.tokenExtractor = tokenExtractor;
			return this;
		}

		public AuthInterceptorBuilder tokenResolver(TokenResolver tokenResolver) {
			this.tokenResolver = tokenResolver;
			return this;
		}

		public AuthInterceptor build() {
			return new AuthInterceptor(tokenExtractor, tokenResolver);
		}
	}
}
