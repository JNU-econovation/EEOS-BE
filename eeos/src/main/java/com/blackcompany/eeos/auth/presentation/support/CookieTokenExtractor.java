package com.blackcompany.eeos.auth.presentation.support;

import com.blackcompany.eeos.auth.application.exception.NotFoundCookieException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("cookie")
@RequiredArgsConstructor
public class CookieTokenExtractor implements TokenExtractor {
	private final CookieNameFormatter cookieNameFormatter;

	@Override
	public String extract(HttpServletRequest request) {
		Cookie[] cookies = getCookies(request);

		for (Cookie cookie : cookies) {
			if (Objects.equals(cookieNameFormatter.format(AuthConstants.TOKEN_KEY), cookie.getName())) {
				return getValue(cookie.getValue());
			}
		}

		throw new NotFoundCookieException();
	}

	private Cookie[] getCookies(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();

		if (cookies == null) {
			throw new NotFoundCookieException();
		}

		return cookies;
	}

	private String getValue(String value) {
		if (value != null) {
			return value;
		}
		throw new NotFoundCookieException();
	}
}
