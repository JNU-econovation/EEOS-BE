package com.blackcompany.eeos.auth.application.service;

import com.blackcompany.eeos.auth.application.domain.token.TokenResolver;
import com.blackcompany.eeos.auth.application.event.DeletedMemberEvent;
import com.blackcompany.eeos.auth.application.repository.OAuthMemberRepository;
import com.blackcompany.eeos.auth.application.usecase.LogOutUsecase;
import com.blackcompany.eeos.auth.application.usecase.WithDrawUsecase;
import com.blackcompany.eeos.auth.persistence.AccountRepository;
import com.blackcompany.eeos.auth.persistence.InvalidTokenRepository;
import com.blackcompany.eeos.member.application.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeactivateMemberService implements LogOutUsecase, WithDrawUsecase {
	private final InvalidTokenRepository invalidTokenRepository;
	private final TokenResolver tokenResolver;
	private final ApplicationEventPublisher eventPublisher;
	private final MemberRepository memberRepository;
	private final OAuthMemberRepository oAuthMemberRepository;
	private final AccountRepository accountRepository;

	@Override
	@Transactional
	public void logOut(final String token, final Long memberId) {
		saveUsedToken(token, memberId);
	}

	@Override
	@Transactional
	public void withDraw(final String token, final Long memberId) {
		if (isNotMember(memberId)) {
			return;
		}

		removeMemberRecord(memberId);
		eventPublisher.publishEvent(DeletedMemberEvent.of(memberId, token));
	}

	private void saveUsedToken(final String token, final Long memberId) {
		invalidTokenRepository.save(token, memberId, getExpiredToken(token));
	}

	private Long getExpiredToken(final String token) {
		return tokenResolver.getExpiredDateByRefreshToken(token);
	}

	private boolean isNotMember(Long memberId) {
		return memberRepository.existsById(memberId);
	}

	private void removeMemberRecord(Long memberId) {
		oAuthMemberRepository.deleteById(memberId);
		accountRepository.deleteById(memberId);
	}
}
