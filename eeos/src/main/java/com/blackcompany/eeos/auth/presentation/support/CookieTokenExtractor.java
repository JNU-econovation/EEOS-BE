package com.blackcompany.eeos.auth.presentation.support;

import com.blackcompany.eeos.auth.application.exception.NotFoundCookieException;
import java.util.Objects;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("cookie")
public class CookieTokenExtractor implements TokenExtractor {
	@Value("${api.cookie-key}")
	private String key;

	@Override
	public String extract(HttpServletRequest request) {
		Cookie[] cookies = getCookies(request);

		for (Cookie cookie : cookies) {
			if (Objects.equals(key, cookie.getName())) {
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
