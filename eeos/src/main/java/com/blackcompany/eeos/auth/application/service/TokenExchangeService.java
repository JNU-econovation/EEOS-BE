package com.blackcompany.eeos.auth.application.service;

import com.blackcompany.eeos.auth.application.domain.AuthorizationCodeData;
import com.blackcompany.eeos.auth.application.domain.PkceValidator;
import com.blackcompany.eeos.auth.application.domain.TokenModel;
import com.blackcompany.eeos.auth.application.exception.InvalidClientException;
import com.blackcompany.eeos.auth.application.exception.InvalidGrantException;
import com.blackcompany.eeos.auth.application.support.AuthenticationTokenGenerator;
import com.blackcompany.eeos.auth.persistence.AuthorizationCodeRepository;
import com.blackcompany.eeos.auth.persistence.client.ClientEntity;
import com.blackcompany.eeos.auth.persistence.client.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenExchangeService {

	private final AuthorizationCodeRepository codeRepository;
	private final ClientRepository clientRepository;
	private final AuthenticationTokenGenerator tokenGenerator;

	public TokenModel exchange(
			String code, String codeVerifier, String redirectUri, String clientId) {
		AuthorizationCodeData codeData =
				codeRepository.findAndDelete(code).orElseThrow(InvalidGrantException::new);

		if (!codeData.getClientId().equals(clientId)) {
			throw new InvalidClientException();
		}

		if (!codeData.getRedirectUri().equals(redirectUri)) {
			throw new InvalidGrantException();
		}

		if (!PkceValidator.validate(
				codeVerifier, codeData.getCodeChallenge(), codeData.getCodeChallengeMethod())) {
			throw new InvalidGrantException();
		}

		ClientEntity client =
				clientRepository
						.findByClientIdWithRedirectUris(clientId)
						.orElseThrow(InvalidClientException::new);

		return tokenGenerator.execute(
				codeData.getMemberId(), client.getClientType().name(), client.getClientId());
	}
}
