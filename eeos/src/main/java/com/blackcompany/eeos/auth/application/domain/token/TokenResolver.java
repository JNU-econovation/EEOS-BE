package com.blackcompany.eeos.auth.application.domain.token;

import com.blackcompany.eeos.auth.application.exception.TokenExpiredException;
import com.blackcompany.eeos.auth.application.exception.TokenParsingException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TokenResolver {

	private static final String MEMBER_ID_CLAIM_KEY = "memberId";
	private static final String ROLE_CLAIM_KEY = "role";
	private final SecretKey accessSecretKey;
	private final SecretKey refreshSecretKey;

	public TokenResolver(
			@Value("${security.jwt.access.secretKey}") String accessSecretKey,
			@Value("${security.jwt.refresh.secretKey}") String refreshSecretKey) {
		this.accessSecretKey = generateSecretKey(accessSecretKey);
		this.refreshSecretKey = generateSecretKey(refreshSecretKey);
	}

	public List<String> getRoles(final String tokens) {
		Claims claims = getAccessClaims(tokens);
		Object roles = claims.get(ROLE_CLAIM_KEY);

		if (roles instanceof List<?>) {
			return ((List<?>) roles)
					.stream()
							.filter(String.class::isInstance)
							.map(String.class::cast)
							.toList();
		}
		//		return (ArrayList<String>) claims.get(ROLE_CLAIM_KEY, ArrayList.class);

		return Collections.emptyList();
	}

	public Long getExpiredDateByAccessToken(final String token) {
		return getExpirationTime(getAccessClaims(token));
	}

	public Long getExpiredDateByRefreshToken(final String token) {
		return getExpirationTime(getRefreshClaims(token));
	}

	public Long getUserDataByAccessToken(final String token) {
		return getClaimValue(getAccessClaims(token), MEMBER_ID_CLAIM_KEY);
	}

	public Long getUserDataByRefreshToken(final String token) {
		return getClaimValue(getRefreshClaims(token), MEMBER_ID_CLAIM_KEY);
	}

	private SecretKey generateSecretKey(String key) {
		return Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
	}

	private Claims getAccessClaims(final String token) {
		return parseClaims(token, accessSecretKey);
	}

	private Claims getRefreshClaims(final String token) {
		return parseClaims(token, refreshSecretKey);
	}

	private Claims parseClaims(final String token, final SecretKey secretKey) {
		try {
			return Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
		} catch (ExpiredJwtException e) {
			throw new TokenExpiredException(e);
		} catch (SignatureException e) {
			throw new TokenParsingException(e);
		} catch (Exception e) {
			log.error("JWT 파싱 중 오류 발생: {}", e.getMessage(), e);
			throw new TokenParsingException(e);
		}
	}

	private Long getExpirationTime(Claims claims) {
		Date expiration = claims.getExpiration();
		return expiration.getTime();
	}

	private Long getClaimValue(Claims claims, String claimKey) {
		return claims.get(claimKey, Long.class);
	}
}
