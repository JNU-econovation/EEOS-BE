package com.blackcompany.eeos.auth.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.blackcompany.eeos.auth.application.domain.token.TokenResolver;
import com.blackcompany.eeos.auth.application.exception.InvalidTokenException;
import com.blackcompany.eeos.auth.application.support.AuthenticationTokenGenerator;
import com.blackcompany.eeos.auth.persistence.MemberAuthenticationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReissueServiceTest {

	@Mock AuthenticationTokenGenerator authenticationTokenGenerator;
	@Mock MemberAuthenticationRepository memberAuthenticationRepository;

	@Mock TokenResolver tokenResolver;
	@InjectMocks ReissueService reissueService;

	@Test
	@DisplayName("블랙리스트에 등록된 토큰이 아니라면 예외가 발생한다.")
	void exception_when_token_invalid() {
		// given
		String token = "token";
		Long memberId = 1L;

		when(tokenResolver.getUserInfoByCookie(token)).thenReturn(memberId);
		when(memberAuthenticationRepository.isExistToken(token)).thenReturn(Boolean.TRUE);

		// when & then
		assertThrows(InvalidTokenException.class, () -> reissueService.execute(token));
	}

	@Test
	@DisplayName("정상적인 토큰이라면 이전에 사용한 토큰은 블랙리스트로 등록하고 새로운 토큰을 생성한다.")
	void token_valid() {
		// given
		String token = "token";
		Long memberId = 1L;
		Long validTime = 1L;

		when(tokenResolver.getUserInfoByCookie(token)).thenReturn(memberId);
		when(memberAuthenticationRepository.isExistToken(token)).thenReturn(Boolean.FALSE);
		when(tokenResolver.getExpiredDateByHeader(token)).thenReturn(validTime);

		// when
		reissueService.execute(token);

		// then
		assertAll(
				() -> verify(memberAuthenticationRepository).save(token, memberId, validTime),
				() -> verify(authenticationTokenGenerator).execute(memberId));
	}
}
