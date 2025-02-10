package com.blackcompany.eeos.auth.presentation.support;

import com.blackcompany.eeos.auth.application.exception.InvalidTemporaryAuthenticationException;
import com.blackcompany.eeos.common.presentation.support.AuthorizationScheme;
import java.util.UUID;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class VerificationAuthorizationResolver implements HandlerMethodArgumentResolver {
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(VerificationId.class);
	}

	@Override
	public Object resolveArgument(
			MethodParameter parameter,
			ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory) {
		String header = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
		if (header == null || !header.startsWith(AuthorizationScheme.VERIFICATION)) {
			throw new InvalidTemporaryAuthenticationException();
		}
		return UUID.fromString(header.substring(AuthorizationScheme.VERIFICATION.length()).trim());
	}
}
