package com.blackcompany.eeos.auth.application.service;

import com.blackcompany.eeos.auth.application.domain.TokenModel;
import com.blackcompany.eeos.auth.application.domain.token.TokenResolver;
import com.blackcompany.eeos.auth.application.exception.InvalidTokenException;
import com.blackcompany.eeos.auth.application.usecase.ReissueUsecase;
import com.blackcompany.eeos.auth.persistence.MemberAuthenticationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ReissueService implements ReissueUsecase {
	private final CreateTokenService createTokenService;
	private final MemberAuthenticationRepository memberAuthenticationRepository;
	private final TokenResolver tokenResolver;

	@Value("${security.jwt.refresh.validTime}")
	private long validTime;

	@Transactional
	@Override
	public TokenModel execute(final String token) {
		Long memberId = tokenResolver.getUserInfoByCookie(token);

		validateToken(token);
		savedUsedToken(token, memberId);

		return createTokenService.execute(memberId);
	}

	private void validateToken(final String token) {
		boolean isExistToken = memberAuthenticationRepository.isExistKey(token);

		if (isExistToken) {
			throw new InvalidTokenException();
		}
	}

	private void savedUsedToken(final String token, final Long memberId) {
		memberAuthenticationRepository.setData(token, memberId, validTime);
	}
}
