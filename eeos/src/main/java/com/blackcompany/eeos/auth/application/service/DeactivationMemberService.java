package com.blackcompany.eeos.auth.application.service;

import com.blackcompany.eeos.auth.application.domain.token.TokenResolver;
import com.blackcompany.eeos.auth.application.event.DeletedMemberEvent;
import com.blackcompany.eeos.auth.application.usecase.LogOutUsecase;
import com.blackcompany.eeos.auth.application.usecase.WithDrawUsecase;
import com.blackcompany.eeos.auth.persistence.BlackAuthenticationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeactivationMemberService implements LogOutUsecase, WithDrawUsecase {
	private final BlackAuthenticationRepository blackAuthenticationRepository;
	private final TokenResolver tokenResolver;
	private final ApplicationEventPublisher eventPublisher;

	@Override
	@Transactional
	public void logOut(final String token, final Long memberId) {
		saveUsedToken(token, memberId);
	}

	@Override
	@Transactional
	public void withDraw(final String token, final Long memberId) {
		saveUsedToken(token, memberId);
		eventPublisher.publishEvent(DeletedMemberEvent.of(memberId));
	}

	private void saveUsedToken(final String token, final Long memberId) {
		blackAuthenticationRepository.save(token, memberId, getExpiredToken(token));
	}

	private Long getExpiredToken(final String token) {
		return tokenResolver.getExpiredDateByRefreshToken(token);
	}
}
