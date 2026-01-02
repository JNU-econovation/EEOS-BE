package com.blackcompany.eeos.auth.presentation.support;

import static com.blackcompany.eeos.common.presentation.support.AuthorizationScheme.BEARER_PREFIX;

import com.blackcompany.eeos.auth.application.exception.NotFoundHeaderTokenException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component("header")
public class HeaderTokenExtractor implements TokenExtractor {

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
		return header.substring(BEARER_PREFIX.length()).trim();
	}

	private void validateHeader(String header) {
		if (!header.toLowerCase().startsWith(BEARER_PREFIX.toLowerCase())) {
			throw new NotFoundHeaderTokenException();
		}
	}
}
