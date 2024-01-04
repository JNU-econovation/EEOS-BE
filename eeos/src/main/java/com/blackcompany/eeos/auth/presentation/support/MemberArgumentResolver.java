package com.blackcompany.eeos.auth.presentation.support;

import com.blackcompany.eeos.auth.application.domain.token.TokenResolver;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.MethodParameter;
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
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		String token = tokenExtractor.extract(request);
		log.error("호출했니?");
		return tokenResolver.getUserInfo(token);
	}
}
