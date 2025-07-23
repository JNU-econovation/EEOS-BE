package com.blackcompany.eeos.config.security;

import com.blackcompany.eeos.auth.application.domain.token.TokenResolver;
import com.blackcompany.eeos.auth.application.exception.NotFoundHeaderTokenException;
import com.blackcompany.eeos.auth.presentation.support.TokenExtractor;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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

	private final TokenExtractor tokenExtractor;
	private final TokenResolver tokenResolver;

	public AccessTokenFilter(
			@Qualifier("header") TokenExtractor tokenExtractor, TokenResolver tokenResolver) {
		this.tokenExtractor = tokenExtractor;
		this.tokenResolver = tokenResolver;
	}

	@Override
	protected void doFilterInternal(
			HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String token = tokenExtractor.extract(request); // tokenExtractor 에서 Auth 헤더가 있는지 검사함

			createAuthentication(token)
					.ifPresentOrElse(this::setAuthentication, SecurityContextHolder::clearContext);

			filterChain.doFilter(request, response);
		} catch (NotFoundHeaderTokenException | JwtException e) {
			SecurityContextHolder.clearContext();
			filterChain.doFilter(request, response);
		}
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
