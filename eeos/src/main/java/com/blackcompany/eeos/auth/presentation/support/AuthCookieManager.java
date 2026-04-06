package com.blackcompany.eeos.auth.presentation.support;

import com.blackcompany.eeos.common.presentation.support.CookieManager;
import com.blackcompany.eeos.common.utils.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthCookieManager implements CookieManager {

	private final CookieNameFormatter cookieNameFormatter;

	private static final Boolean HTTP_ONLY = true;
	private static final Boolean SECURE = true;
	private static final String SAMESITE = "None";
	private static final Long EXPIRATION = 0L;

	@Value("${token.cookie.domain}")
	private String domain;

	@Value("${token.cookie.path}")
	private String path;

	@Value("${security.jwt.access.validTime}")
	private Long accessValidTime;

	@Value("${security.jwt.refresh.validTime}")
	private Long validTime;

	public ResponseCookie setAccessTokenCookie(String value) {
		return ResponseCookie.from(AuthConstants.ACCESS_TOKEN_KEY, value)
				.path("/api")
				.domain(domain)
				.httpOnly(HTTP_ONLY)
				.secure(SECURE)
				.sameSite(SAMESITE)
				.maxAge(TimeUtil.convertSecondsFromMillis(accessValidTime))
				.build();
	}

	public ResponseCookie setRefreshTokenCookie(String value) {
		return ResponseCookie.from(AuthConstants.REFRESH_TOKEN_KEY, value)
				.path("/api/auth")
				.domain(domain)
				.httpOnly(HTTP_ONLY)
				.secure(SECURE)
				.sameSite(SAMESITE)
				.maxAge(TimeUtil.convertSecondsFromMillis(validTime))
				.build();
	}

	public ResponseCookie deleteAccessTokenCookie() {
		return ResponseCookie.from(AuthConstants.ACCESS_TOKEN_KEY, "")
				.path("/api")
				.domain(domain)
				.httpOnly(HTTP_ONLY)
				.secure(SECURE)
				.sameSite(SAMESITE)
				.maxAge(0)
				.build();
	}

	public ResponseCookie deleteRefreshTokenCookie() {
		return ResponseCookie.from(AuthConstants.REFRESH_TOKEN_KEY, "")
				.path("/api/auth")
				.domain(domain)
				.httpOnly(HTTP_ONLY)
				.secure(SECURE)
				.sameSite(SAMESITE)
				.maxAge(0)
				.build();
	}

	@Override
	public ResponseCookie setCookie(String key, String value) {
		return ResponseCookie.from(cookieNameFormatter.format(key), value)
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
		return ResponseCookie.from(cookieNameFormatter.format(key), "")
				.path(path)
				.domain(domain)
				.httpOnly(HTTP_ONLY)
				.secure(SECURE)
				.sameSite(SAMESITE)
				.maxAge(TimeUtil.convertSecondsFromMillis(EXPIRATION))
				.build();
	}
}
