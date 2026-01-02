package com.blackcompany.eeos.common.presentation.support;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AuthorizationSchemeTest {

	@Test
	@DisplayName("Bearer 상수가 올바르게 정의되어 있다")
	void bearer_constant_is_defined() {
		// when & then
		assertEquals("Bearer", AuthorizationScheme.BEARER);
	}

	@Test
	@DisplayName("Bearer prefix 상수가 올바르게 정의되어 있다")
	void bearer_prefix_constant_is_defined() {
		// when & then
		assertEquals("Bearer ", AuthorizationScheme.BEARER_PREFIX);
	}

	@Test
	@DisplayName("formatBearerToken이 올바른 형식의 토큰을 반환한다")
	void format_bearer_token_returns_correct_format() {
		// given
		String token = "test-token-123";

		// when
		String result = AuthorizationScheme.formatBearerToken(token);

		// then
		assertEquals("Bearer test-token-123", result);
	}

	@Test
	@DisplayName("빈 토큰에 대해서도 올바르게 동작한다")
	void format_bearer_token_handles_empty_token() {
		// given
		String token = "";

		// when
		String result = AuthorizationScheme.formatBearerToken(token);

		// then
		assertEquals("Bearer ", result);
	}
}
