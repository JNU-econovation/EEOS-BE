package com.blackcompany.eeos.auth.application.domain.token;

import com.blackcompany.eeos.auth.application.exception.NotFoundCookieException;
import com.blackcompany.eeos.auth.application.exception.NotFoundHeaderTokenException;
import com.blackcompany.eeos.auth.application.exception.RtTokenExpiredException;
import com.blackcompany.eeos.auth.application.exception.TokenExpiredException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TokenResolver {

	private static final String MEMBER_ID_CLAIM_KEY = "memberId";
	private final SecretKey accessSecretKey;
	private final SecretKey refreshSecretKey;

	public TokenResolver(
			@Value("${security.jwt.access.secretKey}") String accessSecretKey,
			@Value("${security.jwt.refresh.secretKey}") String refreshSecretKey) {
		this.accessSecretKey = Keys.hmacShaKeyFor(accessSecretKey.getBytes(StandardCharsets.UTF_8));
		this.refreshSecretKey = Keys.hmacShaKeyFor(refreshSecretKey.getBytes(StandardCharsets.UTF_8));
	}

	public Long getExpiredDateByHeader(final String token) {
		Date expiration = getAccessClaims(token).getExpiration();

		return expiration.getTime();
	}

	public Long getUserInfoByCookie(final String token) {
		return getRefreshClaims(token).get(MEMBER_ID_CLAIM_KEY, Long.class);
	}

	public Long getExpiredDateByCookie(final String token) {
		Date expiration = getRefreshClaims(token).getExpiration();

		return expiration.getTime();
	}

	public Long getUserInfoByHeader(final String token) {
		return getAccessClaims(token).get(MEMBER_ID_CLAIM_KEY, Long.class);
	}

	private Claims getAccessClaims(final String token) {
		try {
			return Jwts.parserBuilder()
					.setSigningKey(accessSecretKey)
					.build()
					.parseClaimsJws(token)
					.getBody();
		} catch (ExpiredJwtException e) {
			throw new TokenExpiredException();
		} catch (SignatureException e) {
			log.error("jwt 파싱에 에러가 발생했습니다.");
			throw new NotFoundHeaderTokenException();
		} catch (IllegalStateException e) {
			throw new NotFoundHeaderTokenException();
		}
	}

	private Claims getRefreshClaims(final String token) {
		try {
			return Jwts.parserBuilder()
					.setSigningKey(refreshSecretKey)
					.build()
					.parseClaimsJws(token)
					.getBody();
		} catch (ExpiredJwtException e) {
			throw new RtTokenExpiredException();
		} catch (SignatureException e) {
			log.error("jwt 파싱에 에러가 발생했습니다.");
			throw new NotFoundCookieException();
		} catch (IllegalStateException e) {
			throw new NotFoundCookieException();
		}
	}
}
