package com.blackcompany.eeos.auth.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.blackcompany.eeos.auth.application.domain.AuthorizationCodeData;
import com.blackcompany.eeos.auth.application.domain.ClientType;
import com.blackcompany.eeos.auth.application.domain.TokenModel;
import com.blackcompany.eeos.auth.application.exception.InvalidClientException;
import com.blackcompany.eeos.auth.application.exception.InvalidGrantException;
import com.blackcompany.eeos.auth.application.support.AuthenticationTokenGenerator;
import com.blackcompany.eeos.auth.persistence.AuthorizationCodeRepository;
import com.blackcompany.eeos.auth.persistence.client.ClientEntity;
import com.blackcompany.eeos.auth.persistence.client.ClientRepository;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TokenExchangeServiceTest {

	@Mock AuthorizationCodeRepository codeRepository;
	@Mock ClientRepository clientRepository;
	@Mock AuthenticationTokenGenerator tokenGenerator;
	@InjectMocks TokenExchangeService tokenExchangeService;

	@Test
	@DisplayName("should exchange code for tokens when PKCE valid")
	void shouldExchangeCodeForTokens() throws Exception {
		// Given
		String codeVerifier = "dBjftJeZ4CVP-mB92K27uhbUJU1p1r_wW1gFWFOEjXk";
		byte[] digest =
				MessageDigest.getInstance("SHA-256")
						.digest(codeVerifier.getBytes(StandardCharsets.US_ASCII));
		String codeChallenge = Base64.getUrlEncoder().withoutPadding().encodeToString(digest);

		AuthorizationCodeData codeData =
				AuthorizationCodeData.builder()
						.memberId(1L)
						.clientId("app-client-id")
						.codeChallenge(codeChallenge)
						.codeChallengeMethod("S256")
						.redirectUri("kr.econovation.eeos://callback")
						.build();

		when(codeRepository.findAndDelete("test-code")).thenReturn(Optional.of(codeData));
		when(clientRepository.findByClientIdWithRedirectUris("app-client-id"))
				.thenReturn(
						Optional.of(
								ClientEntity.builder()
										.clientId("app-client-id")
										.clientType(ClientType.APP)
										.build()));
		when(tokenGenerator.execute(1L, "APP", "app-client-id"))
				.thenReturn(
						TokenModel.builder()
								.accessToken("at")
								.refreshToken("rt")
								.accessExpiredTime(100L)
								.refreshExpiredTime(200L)
								.build());

		// When
		TokenModel result =
				tokenExchangeService.exchange(
						"test-code", codeVerifier, "kr.econovation.eeos://callback", "app-client-id");

		// Then
		assertEquals("at", result.getAccessToken());
	}

	@Test
	@DisplayName("should throw when code expired or not found")
	void shouldThrowWhenCodeNotFound() {
		// Given
		when(codeRepository.findAndDelete("expired-code")).thenReturn(Optional.empty());

		// When & Then
		assertThrows(
				InvalidGrantException.class,
				() -> tokenExchangeService.exchange("expired-code", "verifier", "uri", "client-id"));
	}

	@Test
	@DisplayName("should throw when client_id mismatch")
	void shouldThrowWhenClientIdMismatch() {
		// Given
		AuthorizationCodeData codeData =
				AuthorizationCodeData.builder().clientId("real-client-id").build();
		when(codeRepository.findAndDelete("code")).thenReturn(Optional.of(codeData));

		// When & Then
		assertThrows(
				InvalidClientException.class,
				() -> tokenExchangeService.exchange("code", "verifier", "uri", "wrong-client-id"));
	}

	@Test
	@DisplayName("should throw when redirect_uri mismatch")
	void shouldThrowWhenRedirectUriMismatch() {
		// Given
		AuthorizationCodeData codeData =
				AuthorizationCodeData.builder()
						.clientId("client-id")
						.redirectUri("https://registered.com/callback")
						.build();
		when(codeRepository.findAndDelete("code")).thenReturn(Optional.of(codeData));

		// When & Then
		assertThrows(
				InvalidGrantException.class,
				() ->
						tokenExchangeService.exchange(
								"code", "verifier", "https://other.com/callback", "client-id"));
	}

	@Test
	@DisplayName("should throw when PKCE verification fails")
	void shouldThrowWhenPkceVerificationFails() {
		// Given
		AuthorizationCodeData codeData =
				AuthorizationCodeData.builder()
						.clientId("client-id")
						.redirectUri("https://app.com/callback")
						.codeChallenge("valid-challenge")
						.codeChallengeMethod("S256")
						.build();
		when(codeRepository.findAndDelete("code")).thenReturn(Optional.of(codeData));

		// When & Then
		assertThrows(
				InvalidGrantException.class,
				() ->
						tokenExchangeService.exchange(
								"code", "wrong-verifier", "https://app.com/callback", "client-id"));
	}
}
