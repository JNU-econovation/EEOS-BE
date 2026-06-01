package com.blackcompany.eeos.config.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.blackcompany.eeos.auth.application.domain.token.TokenResolver;
import com.blackcompany.eeos.auth.application.exception.NotFoundHeaderTokenException;
import com.blackcompany.eeos.auth.presentation.support.AuthConstants;
import com.blackcompany.eeos.auth.presentation.support.TokenExtractor;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class AccessTokenFilterTest {

	@Mock private TokenExtractor headerExtractor;
	@Mock private TokenResolver tokenResolver;
	@Mock private HttpServletRequest request;
	@Mock private HttpServletResponse response;
	@Mock private FilterChain filterChain;

	private AccessTokenFilter filter;

	@BeforeEach
	void setUp() {
		SecurityContextHolder.clearContext();
		filter = new AccessTokenFilter(headerExtractor, tokenResolver);
	}

	@Test
	@DisplayName("Authorization 헤더에 토큰이 있으면 헤더에서 추출한다.")
	void extract_from_header_when_present() throws Exception {
		// given
		String token = "valid-access-token";
		when(headerExtractor.extract(request)).thenReturn(token);
		when(tokenResolver.getUserDataByAccessToken(token)).thenReturn(1L);
		when(tokenResolver.getRoles(token)).thenReturn(List.of("ROLE_USER"));

		// when
		filter.doFilterInternal(request, response, filterChain);

		// then
		assertNotNull(SecurityContextHolder.getContext().getAuthentication());
		verify(filterChain).doFilter(request, response);
	}

	@Test
	@DisplayName("헤더에 토큰이 없고 쿠키에 AT가 있으면 쿠키에서 추출한다.")
	void fallback_to_cookie_when_header_absent() throws Exception {
		// given
		String token = "cookie-access-token";
		when(headerExtractor.extract(request)).thenThrow(new NotFoundHeaderTokenException());
		when(request.getCookies())
				.thenReturn(new Cookie[] {new Cookie(AuthConstants.ACCESS_TOKEN_KEY, token)});
		when(tokenResolver.getUserDataByAccessToken(token)).thenReturn(2L);
		when(tokenResolver.getRoles(token)).thenReturn(List.of("ROLE_USER"));

		// when
		filter.doFilterInternal(request, response, filterChain);

		// then
		assertNotNull(SecurityContextHolder.getContext().getAuthentication());
		verify(filterChain).doFilter(request, response);
	}

	@Test
	@DisplayName("헤더도 쿠키도 없으면 SecurityContext를 비우고 필터 체인을 계속한다.")
	void clear_context_when_no_token() throws Exception {
		// given
		when(headerExtractor.extract(request)).thenThrow(new NotFoundHeaderTokenException());
		when(request.getCookies()).thenReturn(null);

		// when
		filter.doFilterInternal(request, response, filterChain);

		// then
		assertNull(SecurityContextHolder.getContext().getAuthentication());
		verify(filterChain).doFilter(request, response);
	}

	@Test
	@DisplayName("PassportFilter가 먼저 JwtAuthentication을 설정한 경우 — 토큰 추출 없이 그대로 통과")
	void skip_token_extraction_when_passport_already_authenticated() throws Exception {
		// given — PassportAuthenticationFilter가 먼저 JwtAuthentication을 설정한 상황
		JwtAuthentication passportAuth =
				new JwtAuthentication(
						42L,
						List.of(
								new org.springframework.security.core.authority.SimpleGrantedAuthority("USER")));
		SecurityContextHolder.getContext().setAuthentication(passportAuth);

		// when
		filter.doFilterInternal(request, response, filterChain);

		// then — headerExtractor 호출 없이 기존 인증 유지
		verify(headerExtractor, never()).extract(any());
		assertNotNull(SecurityContextHolder.getContext().getAuthentication());
		assertEquals(42L, SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		verify(filterChain).doFilter(request, response);
	}
}
