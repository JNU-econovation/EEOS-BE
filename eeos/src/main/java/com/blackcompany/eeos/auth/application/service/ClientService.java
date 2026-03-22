package com.blackcompany.eeos.auth.application.service;

import com.blackcompany.eeos.auth.application.domain.ClientType;
import com.blackcompany.eeos.auth.application.exception.InvalidClientException;
import com.blackcompany.eeos.auth.application.exception.InvalidRedirectUriException;
import com.blackcompany.eeos.auth.persistence.client.ClientEntity;
import com.blackcompany.eeos.auth.persistence.client.ClientRepository;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClientService {

	private static final int MAX_REDIRECT_URIS = 10;
	private static final int MAX_REDIRECT_URI_LENGTH = 512;
	private static final int SECRET_BYTE_LENGTH = 32;

	private final ClientRepository clientRepository;

	@Transactional
	public ClientRegistrationResult register(
			String clientName, ClientType clientType, Set<String> redirectUris) {
		validateRedirectUris(redirectUris);

		String clientId = UUID.randomUUID().toString();
		String rawSecret = null;
		String hashedSecret = null;

		if (clientType.isConfidential()) {
			rawSecret = generateSecret();
			hashedSecret = BCrypt.hashpw(rawSecret, BCrypt.gensalt());
		}

		ClientEntity entity =
				ClientEntity.builder()
						.clientId(clientId)
						.clientSecret(hashedSecret)
						.clientName(clientName)
						.clientType(clientType)
						.build();

		redirectUris.forEach(entity::addRedirectUri);
		clientRepository.save(entity);

		return new ClientRegistrationResult(clientId, rawSecret);
	}

	public ClientEntity findAndValidateRedirectUri(String clientId, String redirectUri) {
		ClientEntity client =
				clientRepository
						.findByClientIdWithRedirectUris(clientId)
						.orElseThrow(InvalidClientException::new);

		if (!client.hasRedirectUri(redirectUri)) {
			throw new InvalidRedirectUriException();
		}

		return client;
	}

	private void validateRedirectUris(Set<String> redirectUris) {
		if (redirectUris == null || redirectUris.isEmpty()) {
			throw new InvalidRedirectUriException();
		}
		if (redirectUris.size() > MAX_REDIRECT_URIS) {
			throw new InvalidRedirectUriException();
		}
		for (String uri : redirectUris) {
			if (uri.length() > MAX_REDIRECT_URI_LENGTH) {
				throw new InvalidRedirectUriException();
			}
		}
	}

	private String generateSecret() {
		byte[] bytes = new byte[SECRET_BYTE_LENGTH];
		new SecureRandom().nextBytes(bytes);
		return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
	}

	public record ClientRegistrationResult(String clientId, String clientSecret) {}
}
