package com.blackcompany.eeos.auth.application.service;

import com.blackcompany.eeos.auth.application.domain.token.TokenResolver;
import com.blackcompany.eeos.auth.application.usecase.LogOutUsecase;
import com.blackcompany.eeos.auth.persistence.BlackAuthenticationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogOutService implements LogOutUsecase {
	private final BlackAuthenticationRepository blackAuthenticationRepository;
	private final TokenResolver tokenResolver;

	@Override
	public void logOut(final String token, final Long memberId) {
		blackAuthenticationRepository.save(token, memberId, getExpiredToken(token));
	}

	private Long getExpiredToken(final String token) {
		return tokenResolver.getExpiredDateByRefreshToken(token);
	}
}
