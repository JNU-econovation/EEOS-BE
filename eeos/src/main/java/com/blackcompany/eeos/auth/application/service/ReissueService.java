package com.blackcompany.eeos.auth.application.service;

import com.blackcompany.eeos.auth.application.domain.TokenModel;
import com.blackcompany.eeos.auth.application.domain.token.TokenResolver;
import com.blackcompany.eeos.auth.application.exception.InvalidTokenException;
import com.blackcompany.eeos.auth.application.support.AuthenticationTokenGenerator;
import com.blackcompany.eeos.auth.application.usecase.ReissueUsecase;
import com.blackcompany.eeos.auth.persistence.MemberAuthenticationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ReissueService implements ReissueUsecase {
	private final AuthenticationTokenGenerator authenticationTokenGenerator;
	private final MemberAuthenticationRepository memberAuthenticationRepository;
	private final TokenResolver tokenResolver;

	@Transactional
	@Override
	public TokenModel execute(final String token) {
		Long memberId = tokenResolver.getUserInfoByCookie(token);

		validateToken(token);
		saveUsedToken(token, memberId);

		return authenticationTokenGenerator.execute(memberId);
	}

	private void validateToken(final String token) {
		boolean isExistToken = memberAuthenticationRepository.isExistToken(token);

		if (isExistToken) {
			throw new InvalidTokenException();
		}
	}

	private void saveUsedToken(final String token, final Long memberId) {
		memberAuthenticationRepository.save(token, memberId, getExpiredToken(token));
	}

	private Long getExpiredToken(final String token) {
		return tokenResolver.getExpiredDateByHeader(token);
	}
}
