package com.blackcompany.eeos.config.security;

import com.blackcompany.eeos.auth.application.domain.token.TokenResolver;
import com.blackcompany.eeos.auth.application.exception.NotFoundHeaderTokenException;
import com.blackcompany.eeos.auth.presentation.support.AuthConstants;
import com.blackcompany.eeos.auth.presentation.support.TokenExtractor;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class AccessTokenFilter extends OncePerRequestFilter {

	private final TokenExtractor headerExtractor;
	private final TokenResolver tokenResolver;

	public AccessTokenFilter(
			@Qualifier("header") TokenExtractor headerExtractor, TokenResolver tokenResolver) {
		this.headerExtractor = headerExtractor;
		this.tokenResolver = tokenResolver;
	}

	@Override
	protected void doFilterInternal(
			HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// Passport 필터(Gateway 경유)가 이미 인증을 설정했으면 스킵
		if (SecurityContextHolder.getContext().getAuthentication() instanceof JwtAuthentication) {
			request.setAttribute("eeos.securityChainProcessed", Boolean.TRUE);
			filterChain.doFilter(request, response);
			return;
		}
		try {
			String token = extractToken(request);

			createAuthentication(token)
					.ifPresentOrElse(this::setAuthentication, SecurityContextHolder::clearContext);

			// Security 체인에서 처리됨을 표시 — UnknownEndpointFilter가 직접 필터로 동작 시 스킵
			request.setAttribute("eeos.securityChainProcessed", Boolean.TRUE);
			filterChain.doFilter(request, response);
		} catch (NotFoundHeaderTokenException | JwtException e) {
			SecurityContextHolder.clearContext();
			// 예외 시에도 Security 체인 처리됨 표시
			request.setAttribute("eeos.securityChainProcessed", Boolean.TRUE);
			filterChain.doFilter(request, response);
		}
	}

	private String extractToken(HttpServletRequest request) {
		try {
			return headerExtractor.extract(request);
		} catch (NotFoundHeaderTokenException e) {
			return extractFromCookie(request);
		}
	}

	private String extractFromCookie(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (AuthConstants.ACCESS_TOKEN_KEY.equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		throw new NotFoundHeaderTokenException();
	}

	private Optional<JwtAuthentication> createAuthentication(String token) {
		Optional<Long> memberId = parseToken(token);

		return memberId.map(
				id ->
						new JwtAuthentication(
								id, parseRole(token).stream().map(SimpleGrantedAuthority::new).toList()));
	}

	private List<String> parseRole(String token) {
		return tokenResolver.getRoles(token);
	}

	private Optional<Long> parseToken(String token) {
		if (token == null) {
			return Optional.empty();
		}
		try {
			return Optional.of(tokenResolver.getUserDataByAccessToken(token));
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	private void setAuthentication(JwtAuthentication authentication) {
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}
