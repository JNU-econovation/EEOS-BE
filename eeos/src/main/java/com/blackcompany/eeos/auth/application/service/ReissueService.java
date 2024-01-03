package com.blackcompany.eeos.auth.application.service;

import com.blackcompany.eeos.auth.application.domain.TokenModel;
import com.blackcompany.eeos.auth.application.domain.token.TokenResolver;
import com.blackcompany.eeos.auth.application.exception.InvalidTokenException;
import com.blackcompany.eeos.auth.application.usecase.ReissueUsecase;
import com.blackcompany.eeos.auth.persistence.AuthInfoEntity;
import com.blackcompany.eeos.auth.persistence.AuthInfoRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ReissueService implements ReissueUsecase {
	private final CreateTokenService createTokenService;
	private final AuthInfoRepository authInfoRepository;
	private final TokenResolver tokenResolver;

	@Transactional
	@Override
	public TokenModel execute(final String token) {
		Long memberId = tokenResolver.getUserInfo(token);
		validateToken(memberId, token);

		return createTokenService.execute(memberId);
	}

	private void validateToken(final Long memberId, final String token) {
		Optional<AuthInfoEntity> validToken =
				authInfoRepository.findByMemberIdAndToken(memberId, token);
		if (validToken.isPresent()) {
			validToken.ifPresent(authInfoRepository::delete);
			return;
		}
		deleteInvalidToken(token);
	}

	private void deleteInvalidToken(final String token) {
		authInfoRepository.findByToken(token).ifPresent(authInfoRepository::delete);
		throw new InvalidTokenException();
	}
}
