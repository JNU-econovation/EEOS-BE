package com.blackcompany.eeos.auth.application.domain.token;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TokenProviderTest {

	private TokenProvider tokenProvider;
	private TokenResolver tokenResolver;

	@BeforeEach
	void setUp() {
		String accessKey = "test-access-secret-key-must-be-at-least-32-characters-long!!";
		String refreshKey = "test-refresh-secret-key-must-be-at-least-32-characters-long!";
		long accessValidTime = 3600000L;
		long refreshValidTime = 86400000L;

		tokenProvider = new TokenProvider(accessKey, refreshKey, accessValidTime, refreshValidTime);
		tokenResolver = new TokenResolver(accessKey, refreshKey);
	}

	@Test
	@DisplayName("should create refresh token with clientType and clientId claims")
	void shouldCreateRefreshTokenWithClientClaims() {
		// When
		String rt = tokenProvider.createRefreshToken(1L, "WEB", "test-client-id");

		// Then
		assertEquals("WEB", tokenResolver.getClientTypeByRefreshToken(rt));
		assertEquals("test-client-id", tokenResolver.getClientIdByRefreshToken(rt));
		assertEquals(1L, tokenResolver.getUserDataByRefreshToken(rt));
	}

	@Test
	@DisplayName("should create refresh token for APP with clientType APP")
	void shouldCreateRefreshTokenForApp() {
		// When
		String rt = tokenProvider.createRefreshToken(2L, "APP", "app-client-id");

		// Then
		assertEquals("APP", tokenResolver.getClientTypeByRefreshToken(rt));
		assertEquals("app-client-id", tokenResolver.getClientIdByRefreshToken(rt));
	}

	@Test
	@DisplayName("should return null clientType for legacy refresh token")
	void shouldReturnNullClientTypeForLegacyRefreshToken() {
		// Given — legacy RT without client claims
		String rt = tokenProvider.createRefreshToken(3L);

		// When
		String clientType = tokenResolver.getClientTypeByRefreshToken(rt);

		// Then
		assertNull(clientType);
	}
}
