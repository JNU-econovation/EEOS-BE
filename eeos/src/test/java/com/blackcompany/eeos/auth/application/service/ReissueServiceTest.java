package com.blackcompany.eeos.auth.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.blackcompany.eeos.auth.application.domain.token.TokenResolver;
import com.blackcompany.eeos.auth.application.exception.InvalidTokenException;
import com.blackcompany.eeos.auth.fixture.FakeAuthInfo;
import com.blackcompany.eeos.auth.persistence.AuthInfoEntity;
import com.blackcompany.eeos.auth.persistence.AuthInfoRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReissueServiceTest {

	@Mock CreateTokenService createTokenService;
	@Mock AuthInfoRepository authInfoRepository;

	@Mock TokenResolver tokenResolver;
	@InjectMocks ReissueService reissueService;

	@Test
	@DisplayName("전달받은 토큰이 서버가 가지고 있는 유저의 토큰이 아닐 때  유효하지 않은 토큰을 제거하고 예외를 발생시킨다.")
	void exception_when_token_invalid() {
		// given
		String token = "token";
		Long memberId = 1L;

		AuthInfoEntity authInfoEntity = FakeAuthInfo.authInfoEntity();

		when(tokenResolver.getUserInfoByCookie(token)).thenReturn(memberId);
		when(authInfoRepository.findByMemberIdAndToken(memberId, token))
				.thenReturn(Optional.ofNullable(null));
		when(authInfoRepository.findByToken(token)).thenReturn(Optional.ofNullable(authInfoEntity));

		// when & then
		assertAll(
				() -> assertThrows(InvalidTokenException.class, () -> reissueService.execute(token)),
				() -> verify(authInfoRepository).delete(authInfoEntity));
	}

	@Test
	@DisplayName("전달받은 토큰이 유효한 토큰이라면 해당 토큰 정보를 제거하고 새로운 토큰을 생성한다.")
	void execute() {
		// given
		String token = "token";
		Long memberId = 1L;

		AuthInfoEntity authInfoEntity = FakeAuthInfo.authInfoEntity();

		when(tokenResolver.getUserInfoByCookie(token)).thenReturn(memberId);
		when(authInfoRepository.findByMemberIdAndToken(memberId, token))
				.thenReturn(Optional.ofNullable(authInfoEntity));

		// when
		reissueService.execute(token);

		// then
		assertAll(
				() -> verify(authInfoRepository).delete(authInfoEntity),
				() -> verify(createTokenService).execute(memberId));
	}
}
