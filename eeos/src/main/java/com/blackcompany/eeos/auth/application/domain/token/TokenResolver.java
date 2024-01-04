package com.blackcompany.eeos.auth.application.domain.token;

import com.blackcompany.eeos.auth.application.exception.TokenExpiredException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TokenResolver {

	private static final String MEMBER_ID_CLAIM_KEY = "memberId";
	private final SecretKey secretKey;

	public TokenResolver(@Value("${security.jwt.token.secretKey}") String accessSecretKey) {
		this.secretKey = Keys.hmacShaKeyFor(accessSecretKey.getBytes(StandardCharsets.UTF_8));
	}

	private Claims getClaims(final String token) {
		try {
			return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
		} catch (ExpiredJwtException e) {
			throw new TokenExpiredException();
		}
	}

	public Long getExpiredDate(final String token) {
		Objects.requireNonNull(token);
		Date expiration = getClaims(token).getExpiration();

		return expiration.getTime();
	}

	public Long getUserInfo(final String token) {
		Objects.requireNonNull(token);
		return getClaims(token).get(MEMBER_ID_CLAIM_KEY, Long.class);
	}
}
