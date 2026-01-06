package com.blackcompany.eeos.acceptance.common;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;

public class TokenProvider {

	private static final String SECRET_KEY =
			"dGVzdC1zZWNyZXQta2V5LWZvci1pbnRlZ3JhdGlvbi10ZXN0LTEyMzQ1Njc4OTA=";
	private static final long ACCESS_TOKEN_VALIDITY = 3600 * 1000;

	public static String createAdminToken(Long memberId) {
		return createToken(memberId, "ADMIN");
	}

	public static String createUserToken(Long memberId) {
		return createToken(memberId, "USER");
	}

	private static String createToken(Long memberId, String role) {
		final Date now = Date.from(Instant.now());

		try {
			return Jwts.builder()
					.setHeaderParam(Header.TYPE, Header.JWT_TYPE)
					.claim("memberId", memberId)
					.claim("role", role)
					.setIssuedAt(now)
					.setExpiration(new Date(now.getTime() + ACCESS_TOKEN_VALIDITY))
					.signWith(getSigningKey())
					.compact();
		} catch (Exception e) {
			throw new RuntimeException("JWT 토큰 생성 중 에러 발생", e);
		}
	}

	private static Key getSigningKey() {
		byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
