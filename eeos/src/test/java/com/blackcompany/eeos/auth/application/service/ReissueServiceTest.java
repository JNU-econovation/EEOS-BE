package com.blackcompany.eeos.auth.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.blackcompany.eeos.auth.application.domain.TokenModel;
import com.blackcompany.eeos.auth.application.domain.token.TokenResolver;
import com.blackcompany.eeos.auth.application.exception.InvalidTokenException;
import com.blackcompany.eeos.auth.application.support.AuthenticationTokenGenerator;
import com.blackcompany.eeos.auth.persistence.InvalidTokenRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReissueServiceTest {

	@Mock AuthenticationTokenGenerator authenticationTokenGenerator;
	@Mock InvalidTokenRepository invalidTokenRepository;
	@Mock TokenResolver tokenResolver;

	@InjectMocks ReissueService reissueService;

	@Test
	@DisplayName("블랙리스트에 등록된 토큰이라면 예외가 발생한다.")
	void exception_when_token_invalid() {
		// given
		String token = "token";
		when(invalidTokenRepository.isExistToken(token)).thenReturn(true);

		// when & then
		assertThrows(InvalidTokenException.class, () -> reissueService.execute(token));
	}

	@Test
	@DisplayName("정상적인 토큰이라면 이전에 사용한 토큰은 블랙리스트로 등록하고 새로운 토큰을 생성한다.")
	void token_valid() {
		// given
		String token = "token";
		Long memberId = 2L;
		Long expiredTime = System.currentTimeMillis() + 60000L;
		TokenModel expectedToken = TokenModel.builder().accessToken("at").refreshToken("rt").build();

		when(invalidTokenRepository.isExistToken(token)).thenReturn(false);
		when(tokenResolver.getUserDataByRefreshToken(token)).thenReturn(memberId);
		when(tokenResolver.getExpiredDateByRefreshToken(token)).thenReturn(expiredTime);
		when(tokenResolver.getClientTypeByRefreshToken(token)).thenReturn(null);
		when(authenticationTokenGenerator.execute(memberId)).thenReturn(expectedToken);

		// when
		TokenModel result = reissueService.execute(token);

		// then
		assertAll(
				() -> verify(invalidTokenRepository).save(token, memberId, expiredTime),
				() -> verify(authenticationTokenGenerator).execute(memberId),
				() -> assertEquals(expectedToken, result));
	}

	@Test
	@DisplayName("client claims가 있는 RT로 재발급하면 clientType/clientId가 보존된다.")
	void reissue_preserves_client_claims() {
		// given
		String token = "token-with-client";
		Long memberId = 3L;
		Long expiredTime = System.currentTimeMillis() + 60000L;
		String clientType = "WEB";
		String clientId = "web-client-id";
		TokenModel expectedToken = TokenModel.builder().accessToken("at").refreshToken("rt").build();

		when(invalidTokenRepository.isExistToken(token)).thenReturn(false);
		when(tokenResolver.getUserDataByRefreshToken(token)).thenReturn(memberId);
		when(tokenResolver.getExpiredDateByRefreshToken(token)).thenReturn(expiredTime);
		when(tokenResolver.getClientTypeByRefreshToken(token)).thenReturn(clientType);
		when(tokenResolver.getClientIdByRefreshToken(token)).thenReturn(clientId);
		when(authenticationTokenGenerator.execute(memberId, clientType, clientId))
				.thenReturn(expectedToken);

		// when
		TokenModel result = reissueService.execute(token);

		// then
		assertAll(
				() -> verify(invalidTokenRepository).save(token, memberId, expiredTime),
				() -> verify(authenticationTokenGenerator).execute(memberId, clientType, clientId),
				() -> verify(authenticationTokenGenerator, never()).execute(memberId),
				() -> assertEquals(expectedToken, result));
	}
}
