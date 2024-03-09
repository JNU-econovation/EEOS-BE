package com.blackcompany.eeos.auth.presentation.support;

import com.blackcompany.eeos.common.presentation.support.CookieManager;
import com.blackcompany.eeos.common.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class AuthCookieManager implements CookieManager {

	private static final Boolean HTTP_ONLY = true;
	private static final Boolean SECURE = true;
	private static final String SAMESITE = "None";
	private static final Long EXPIRATION = 0L;

	@Value("${token.cookie.domain}")
	private String domain;

	@Value("${token.cookie.path}")
	private String path;

	@Value("${security.jwt.refresh.validTime}")
	private Long validTime;

	@Override
	public ResponseCookie setCookie(String key, String value) {
		return ResponseCookie.from(key, value)
				.path(path)
				.domain(domain)
				.httpOnly(HTTP_ONLY)
				.secure(SECURE)
				.sameSite(SAMESITE)
				.maxAge(TimeUtil.convertSecondsFromMillis(validTime))
				.build();
	}

	@Override
	public ResponseCookie deleteCookie(String key) {
		return ResponseCookie.from(key, "")
				.path(path)
				.domain(domain)
				.httpOnly(HTTP_ONLY)
				.secure(SECURE)
				.sameSite(SAMESITE)
				.maxAge(TimeUtil.convertSecondsFromMillis(EXPIRATION))
				.build();
	}
}
