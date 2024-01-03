package com.blackcompany.eeos.auth.presentation.support;

import com.blackcompany.eeos.auth.application.exception.NotFoundHeaderTokenException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class HeaderTokenExtractor implements TokenExtractor {
	private static final String BEARER_TOKEN_PREFIX = "Bearer ";

	@Override
	public String extract(HttpServletRequest request) {
		String header = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (header == null) {
			throw new NotFoundHeaderTokenException();
		}
		return extractToken(header);
	}

	private String extractToken(String header) {
		validateHeader(header);
		return header.substring(BEARER_TOKEN_PREFIX.length()).trim();
	}

	private void validateHeader(String header) {
		if (!header.toLowerCase().startsWith(BEARER_TOKEN_PREFIX.toLowerCase())) {
			throw new NotFoundHeaderTokenException();
		}
	}
}
