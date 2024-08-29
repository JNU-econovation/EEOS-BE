package com.blackcompany.eeos.auth.application.service;

import com.blackcompany.eeos.auth.application.domain.TokenModel;
import com.blackcompany.eeos.auth.application.domain.token.TokenResolver;
import com.blackcompany.eeos.auth.application.exception.InvalidTokenException;
import com.blackcompany.eeos.auth.application.support.AuthenticationTokenGenerator;
import com.blackcompany.eeos.auth.application.usecase.ReissueUsecase;
import com.blackcompany.eeos.auth.persistence.InvalidTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReissueService implements ReissueUsecase {
	private final AuthenticationTokenGenerator authenticationTokenGenerator;
	private final InvalidTokenRepository invalidTokenRepository;
	private final TokenResolver tokenResolver;

	@Transactional
	@Override
	public TokenModel execute(final String token) {
		validateToken(token);

		Long memberId = tokenResolver.getUserDataByRefreshToken(token);
		saveUsedToken(token, memberId);

		return authenticationTokenGenerator.execute(memberId);
	}

	private void validateToken(final String token) {
		boolean isExistToken = invalidTokenRepository.isExistToken(token);

		if (isExistToken) {
			throw new InvalidTokenException();
		}
	}

	private void saveUsedToken(final String token, final Long memberId) {
		invalidTokenRepository.save(token, memberId, getExpiredToken(token));
	}

	private Long getExpiredToken(final String token) {
		return tokenResolver.getExpiredDateByRefreshToken(token);
	}
}
