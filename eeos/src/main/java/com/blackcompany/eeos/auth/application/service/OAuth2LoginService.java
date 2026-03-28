package com.blackcompany.eeos.auth.application.service;

import com.blackcompany.eeos.auth.application.domain.AuthorizationCodeData;
import com.blackcompany.eeos.auth.application.domain.TokenModel;
import com.blackcompany.eeos.auth.application.support.AuthenticationTokenGenerator;
import com.blackcompany.eeos.auth.application.support.LoginRateLimiter;
import com.blackcompany.eeos.auth.persistence.AuthorizationCodeRepository;
import com.blackcompany.eeos.auth.persistence.client.ClientEntity;
import com.blackcompany.eeos.member.application.model.MemberModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuth2LoginService {

	private final AuthService authService;
	private final ClientService clientService;
	private final AuthenticationTokenGenerator tokenGenerator;
	private final AuthorizationCodeRepository codeRepository;
	private final LoginRateLimiter rateLimiter;

	public TokenModel loginForWeb(
			String clientId, String redirectUri, String email, String password, String ip) {
		rateLimiter.checkRateLimit(ip, email);
		ClientEntity client = clientService.findAndValidateRedirectUri(clientId, redirectUri);

		MemberModel member = authenticateWithRateLimit(email, password, ip);
		rateLimiter.resetAccountCounter(email);

		return tokenGenerator.execute(
				member.getMemberId(), client.getClientType().name(), client.getClientId());
	}

	public String loginForApp(
			String clientId,
			String redirectUri,
			String email,
			String password,
			String ip,
			String codeChallenge,
			String codeChallengeMethod) {
		rateLimiter.checkRateLimit(ip, email);
		ClientEntity client = clientService.findAndValidateRedirectUri(clientId, redirectUri);

		MemberModel member = authenticateWithRateLimit(email, password, ip);
		rateLimiter.resetAccountCounter(email);

		AuthorizationCodeData codeData =
				AuthorizationCodeData.builder()
						.memberId(member.getMemberId())
						.clientId(client.getClientId())
						.codeChallenge(codeChallenge)
						.codeChallengeMethod(codeChallengeMethod)
						.redirectUri(redirectUri)
						.build();

		return codeRepository.save(codeData);
	}

	private MemberModel authenticateWithRateLimit(String email, String password, String ip) {
		try {
			return authService.authenticate(email, password);
		} catch (Exception e) {
			rateLimiter.recordFailure(ip, email);
			throw e;
		}
	}
}
