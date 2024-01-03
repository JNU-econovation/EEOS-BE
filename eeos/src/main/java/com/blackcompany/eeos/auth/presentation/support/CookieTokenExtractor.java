package com.blackcompany.eeos.auth.presentation.support;

import com.blackcompany.eeos.auth.application.exception.NotFoundCookieException;
import java.util.Objects;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component("cookie")
public class CookieTokenExtractor implements TokenExtractor {
	private static final String COOKIE_KEY = "token";

	@Override
	public String extract(HttpServletRequest request) {
		Cookie[] cookies = getCookies(request);

		for (Cookie cookie : cookies) {
			if (Objects.equals(COOKIE_KEY, cookie.getName())) {
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
