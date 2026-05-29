package com.blackcompany.eeos.auth.presentation.support;

import com.blackcompany.eeos.auth.application.domain.token.TokenResolver;
import com.blackcompany.eeos.auth.application.exception.NotFoundHeaderTokenException;
import com.blackcompany.eeos.config.security.JwtAuthentication;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@Slf4j
public class MemberArgumentResolver implements HandlerMethodArgumentResolver {
	private final TokenExtractor tokenExtractor;
	private final TokenResolver tokenResolver;

	public MemberArgumentResolver(
			@Qualifier("header") TokenExtractor tokenExtractor, TokenResolver tokenResolver) {
		this.tokenExtractor = tokenExtractor;
		this.tokenResolver = tokenResolver;
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(Member.class);
	}

	@Override
	public Object resolveArgument(
			MethodParameter parameter,
			ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory) {
		// Passport 필터(Gateway 경유)가 이미 SecurityContext를 채웠으면 그 값 사용
		var authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof JwtAuthentication jwtAuth) {
			return jwtAuth.getPrincipal();
		}

		// 직접 호출(EEOS 자체 토큰): 기존 방식 유지
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		String token = tokenExtractor.extract(request);
		if (token == null) {
			Member annotation = parameter.getParameterAnnotation(Member.class);
			if (annotation.required()) {
				throw new NotFoundHeaderTokenException();
			}
			return null;
		}
		return tokenResolver.getUserDataByAccessToken(token);
	}
}
