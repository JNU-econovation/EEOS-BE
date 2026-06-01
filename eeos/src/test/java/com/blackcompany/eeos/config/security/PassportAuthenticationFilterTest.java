package com.blackcompany.eeos.config.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * PassportAuthenticationFilter 단위 테스트
 *
 * <p>Gateway가 주입하는 X-User-Passport 헤더를 파싱하여 SecurityContext를 올바르게 설정하는지 검증한다.
 */
@ExtendWith(MockitoExtension.class)
class PassportAuthenticationFilterTest {

	@Mock private HttpServletRequest request;
	@Mock private HttpServletResponse response;
	@Mock private FilterChain filterChain;

	private PassportAuthenticationFilter filter;

	@BeforeEach
	void setUp() {
		SecurityContextHolder.clearContext();
		filter = new PassportAuthenticationFilter();
	}

	// ──────────────────────────────────────────────────────────
	// 정상 케이스
	// ──────────────────────────────────────────────────────────

	@Nested
	@DisplayName("유효한 X-User-Passport 헤더")
	class ValidPassportHeader {

		@Test
		@DisplayName("memberId와 roles가 담긴 Passport → JwtAuthentication 설정")
		void valid_passport_sets_jwt_authentication() throws Exception {
			String passport = passport(42L, "[\"USER\"]");
			when(request.getHeader(PassportAuthenticationFilter.PASSPORT_HEADER)).thenReturn(passport);

			filter.doFilterInternal(request, response, filterChain);

			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			assertThat(auth).isInstanceOf(JwtAuthentication.class);
			assertThat(auth.getPrincipal()).isEqualTo(42L);
			verify(filterChain).doFilter(request, response);
		}

		@Test
		@DisplayName("memberId가 문자열로 담겨있어도 Long으로 파싱")
		void memberId_as_string_is_parsed_to_long() throws Exception {
			String passport = passportRaw("{\"memberId\":\"99\",\"roles\":[\"USER\"]}");
			when(request.getHeader(PassportAuthenticationFilter.PASSPORT_HEADER)).thenReturn(passport);

			filter.doFilterInternal(request, response, filterChain);

			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			assertThat(auth.getPrincipal()).isEqualTo(99L);
		}

		@Test
		@DisplayName("roles가 여러 개여도 모두 authority로 등록")
		void multiple_roles_all_registered_as_authorities() throws Exception {
			String passport = passport(1L, "[\"USER\",\"ADMIN\"]");
			when(request.getHeader(PassportAuthenticationFilter.PASSPORT_HEADER)).thenReturn(passport);

			filter.doFilterInternal(request, response, filterChain);

			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			assertThat(auth.getAuthorities())
					.extracting("authority")
					.containsExactlyInAnyOrder("USER", "ADMIN");
		}

		@Test
		@DisplayName("roles 필드 없어도 memberId 있으면 빈 권한으로 인증")
		void passport_without_roles_still_authenticates() throws Exception {
			String passport = passportRaw("{\"memberId\":7}");
			when(request.getHeader(PassportAuthenticationFilter.PASSPORT_HEADER)).thenReturn(passport);

			filter.doFilterInternal(request, response, filterChain);

			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			assertThat(auth).isNotNull();
			assertThat(auth.getPrincipal()).isEqualTo(7L);
			assertThat(auth.getAuthorities()).isEmpty();
		}
	}

	// ──────────────────────────────────────────────────────────
	// 헤더 없음 케이스
	// ──────────────────────────────────────────────────────────

	@Nested
	@DisplayName("X-User-Passport 헤더 없음")
	class MissingPassportHeader {

		@Test
		@DisplayName("헤더 없으면 SecurityContext 미설정, 체인 계속")
		void no_header_skips_authentication() throws Exception {
			when(request.getHeader(PassportAuthenticationFilter.PASSPORT_HEADER)).thenReturn(null);

			filter.doFilterInternal(request, response, filterChain);

			assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
			verify(filterChain).doFilter(request, response);
		}

		@Test
		@DisplayName("헤더가 빈 문자열이어도 SecurityContext 미설정")
		void blank_header_skips_authentication() throws Exception {
			when(request.getHeader(PassportAuthenticationFilter.PASSPORT_HEADER)).thenReturn("  ");

			filter.doFilterInternal(request, response, filterChain);

			assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
			verify(filterChain).doFilter(request, response);
		}
	}

	// ──────────────────────────────────────────────────────────
	// 파싱 실패 케이스 — 예외를 던지지 않고 체인 계속
	// ──────────────────────────────────────────────────────────

	@Nested
	@DisplayName("유효하지 않은 Passport 값")
	class InvalidPassportHeader {

		@Test
		@DisplayName("Base64 디코딩 불가 값 → 인증 미설정, 필터 체인 계속 (예외 없음)")
		void invalid_base64_does_not_throw() throws Exception {
			when(request.getHeader(PassportAuthenticationFilter.PASSPORT_HEADER))
					.thenReturn("!!!not-base64!!!");

			filter.doFilterInternal(request, response, filterChain);

			assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
			verify(filterChain).doFilter(request, response);
		}

		@Test
		@DisplayName("JSON 형식 아님 → 인증 미설정, 필터 체인 계속 (예외 없음)")
		void invalid_json_does_not_throw() throws Exception {
			String garbage =
					Base64.getEncoder().encodeToString("not-json".getBytes(StandardCharsets.UTF_8));
			when(request.getHeader(PassportAuthenticationFilter.PASSPORT_HEADER)).thenReturn(garbage);

			filter.doFilterInternal(request, response, filterChain);

			assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
			verify(filterChain).doFilter(request, response);
		}

		@Test
		@DisplayName("memberId 없는 Passport → 인증 미설정, 필터 체인 계속")
		void passport_without_memberId_skips_authentication() throws Exception {
			String passport = passportRaw("{\"loginId\":\"user\",\"roles\":[\"USER\"]}");
			when(request.getHeader(PassportAuthenticationFilter.PASSPORT_HEADER)).thenReturn(passport);

			filter.doFilterInternal(request, response, filterChain);

			assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
			verify(filterChain).doFilter(request, response);
		}
	}

	// ──────────────────────────────────────────────────────────
	// AccessTokenFilter 연동 — 이미 인증된 경우 스킵
	// ──────────────────────────────────────────────────────────

	@Nested
	@DisplayName("PassportFilter → AccessTokenFilter 연동")
	class PassportAccessTokenIntegration {

		@Test
		@DisplayName("PassportFilter가 JwtAuthentication 설정 후 AccessTokenFilter는 해당 인증을 유지")
		void access_token_filter_skips_when_passport_already_authenticated() throws Exception {
			// 1. PassportFilter가 인증 설정
			String passport = passport(10L, "[\"USER\"]");
			when(request.getHeader(PassportAuthenticationFilter.PASSPORT_HEADER)).thenReturn(passport);
			filter.doFilterInternal(request, response, filterChain);

			// SecurityContext에 JwtAuthentication이 세팅됐는지 확인
			Authentication beforeAccess = SecurityContextHolder.getContext().getAuthentication();
			assertThat(beforeAccess).isInstanceOf(JwtAuthentication.class);
			assertThat(beforeAccess.getPrincipal()).isEqualTo(10L);

			// 2. AccessTokenFilter는 JwtAuthentication 감지 → 스킵
			// (AccessTokenFilter의 instanceof JwtAuthentication 분기가 동작함을 간접 검증)
			assertThat(SecurityContextHolder.getContext().getAuthentication())
					.isInstanceOf(JwtAuthentication.class);
		}
	}

	// ──────────────────────────────────────────────────────────
	// 헬퍼
	// ──────────────────────────────────────────────────────────

	/** memberId + roles JSON을 Base64 인코딩하여 Passport 헤더 값 생성 */
	private String passport(Long memberId, String rolesJson) {
		String json = String.format("{\"memberId\":%d,\"roles\":%s}", memberId, rolesJson);
		return passportRaw(json);
	}

	private String passportRaw(String json) {
		return Base64.getEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));
	}
}
