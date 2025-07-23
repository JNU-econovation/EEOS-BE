package com.blackcompany.eeos.auth.application.domain.token;

import com.blackcompany.eeos.auth.application.model.Role;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;

public class JwtTestUtil {

	public static String createToken(Long memberId, Role role) {
		final Date now = Date.from(Instant.now());
		final long accessValidTime = 3600 * 1000;

		try {
			return Jwts.builder()
					.setHeaderParam(Header.TYPE, Header.JWT_TYPE)
					.claim("memberId", memberId)
					.claim("role", role.name())
					.setIssuedAt(now)
					.setExpiration(new Date(now.getTime() + accessValidTime))
					.signWith(createRandomKey())
					.compact();
		} catch (Exception e) {
			throw new RuntimeException("[테스트] JWT 토큰 생성 중 예상치 못한 에러");
		}
	}

	private static Key createRandomKey() throws Exception {
		String stringKey =
				java.util.Base64.getEncoder()
						.encodeToString(java.security.SecureRandom.getInstanceStrong().generateSeed(32));

		return Keys.hmacShaKeyFor(stringKey.getBytes(StandardCharsets.UTF_8));
	}
}
