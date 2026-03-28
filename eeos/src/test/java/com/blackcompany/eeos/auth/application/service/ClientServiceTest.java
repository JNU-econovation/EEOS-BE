package com.blackcompany.eeos.auth.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.blackcompany.eeos.auth.application.domain.ClientType;
import com.blackcompany.eeos.auth.application.exception.InvalidClientException;
import com.blackcompany.eeos.auth.application.exception.InvalidRedirectUriException;
import com.blackcompany.eeos.auth.persistence.client.ClientEntity;
import com.blackcompany.eeos.auth.persistence.client.ClientRepository;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

	@Mock ClientRepository clientRepository;
	@InjectMocks ClientService clientService;

	@Test
	@DisplayName("should register WEB client with secret")
	void shouldRegisterWebClientWithSecret() {
		// Given
		when(clientRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

		// When
		var result =
				clientService.register(
						"EEOS-Web", ClientType.WEB, Set.of("https://eeos.econovation.kr/callback"));

		// Then
		assertNotNull(result.clientId());
		assertNotNull(result.clientSecret());
	}

	@Test
	@DisplayName("should register APP client without secret")
	void shouldRegisterAppClientWithoutSecret() {
		// Given
		when(clientRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

		// When
		var result =
				clientService.register(
						"EEOS-App", ClientType.APP, Set.of("kr.econovation.eeos://callback"));

		// Then
		assertNotNull(result.clientId());
		assertNull(result.clientSecret());
	}

	@Test
	@DisplayName("should throw when redirectUris is empty")
	void shouldThrowWhenRedirectUrisIsEmpty() {
		// When & Then
		assertThrows(
				InvalidRedirectUriException.class,
				() -> clientService.register("Test", ClientType.WEB, Set.of()));
	}

	@Test
	@DisplayName("should throw when redirectUris exceeds 10")
	void shouldThrowWhenRedirectUrisExceedsTen() {
		// Given
		Set<String> uris = Set.of("u1", "u2", "u3", "u4", "u5", "u6", "u7", "u8", "u9", "u10", "u11");

		// When & Then
		assertThrows(
				InvalidRedirectUriException.class,
				() -> clientService.register("Test", ClientType.WEB, uris));
	}

	@Test
	@DisplayName("should throw when redirectUri exceeds 512 chars")
	void shouldThrowWhenRedirectUriExceedsMaxLength() {
		// Given
		String longUri = "https://example.com/" + "a".repeat(500);

		// When & Then
		assertThrows(
				InvalidRedirectUriException.class,
				() -> clientService.register("Test", ClientType.WEB, Set.of(longUri)));
	}

	@Test
	@DisplayName("should find client and validate redirectUri")
	void shouldFindClientAndValidateRedirectUri() {
		// Given
		ClientEntity entity =
				ClientEntity.builder()
						.clientId("test-uuid")
						.clientType(ClientType.WEB)
						.clientName("Test")
						.build();
		entity.addRedirectUri("https://example.com/callback");
		when(clientRepository.findByClientIdWithRedirectUris("test-uuid"))
				.thenReturn(Optional.of(entity));

		// When
		ClientEntity found =
				clientService.findAndValidateRedirectUri("test-uuid", "https://example.com/callback");

		// Then
		assertEquals("test-uuid", found.getClientId());
	}

	@Test
	@DisplayName("should throw when clientId not found")
	void shouldThrowWhenClientIdNotFound() {
		// Given
		when(clientRepository.findByClientIdWithRedirectUris("unknown")).thenReturn(Optional.empty());

		// When & Then
		assertThrows(
				InvalidClientException.class,
				() -> clientService.findAndValidateRedirectUri("unknown", "https://any.com"));
	}

	@Test
	@DisplayName("should throw when redirectUri not registered")
	void shouldThrowWhenRedirectUriNotRegistered() {
		// Given
		ClientEntity entity =
				ClientEntity.builder()
						.clientId("test-uuid")
						.clientType(ClientType.WEB)
						.clientName("Test")
						.build();
		entity.addRedirectUri("https://registered.com/callback");
		when(clientRepository.findByClientIdWithRedirectUris("test-uuid"))
				.thenReturn(Optional.of(entity));

		// When & Then
		assertThrows(
				InvalidRedirectUriException.class,
				() ->
						clientService.findAndValidateRedirectUri(
								"test-uuid", "https://unregistered.com/callback"));
	}
}
