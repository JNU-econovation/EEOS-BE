package com.blackcompany.eeos.auth.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.blackcompany.eeos.auth.application.domain.token.TokenResolver;
import com.blackcompany.eeos.auth.application.exception.InvalidTokenException;
import com.blackcompany.eeos.auth.application.support.AuthenticationTokenGenerator;
import com.blackcompany.eeos.auth.persistence.InvalidTokenRepository;
import com.blackcompany.eeos.common.DataClearExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

@SpringBootTest
@ExtendWith(DataClearExtension.class)
class ReissueServiceTest {

	@SpyBean AuthenticationTokenGenerator authenticationTokenGenerator;
	@Autowired InvalidTokenRepository invalidTokenRepository;
	@MockBean TokenResolver tokenResolver;
	@Autowired ReissueService reissueService;

	@Test
	@DisplayName("블랙리스트에 등록된 토큰이라면 예외가 발생한다.")
	void exception_when_token_invalid() {
		// given
		String token = "token";
		Long memberId = 2L;
		Long validTime = 1000L * 1;

		invalidTokenRepository.save(token, memberId, validTime);

		// when & then
		assertThrows(InvalidTokenException.class, () -> reissueService.execute(token));
	}

	@Test
	@DisplayName("정상적인 토큰이라면 이전에 사용한 토큰은 블랙리스트로 등록하고 새로운 토큰을 생성한다.")
	void token_valid() {
		// given
		String token = "token";
		Long memberId = 2L;
		Long validTime = 1000L * 1;

		when(tokenResolver.getUserDataByRefreshToken(token)).thenReturn(memberId);
		when(tokenResolver.getExpiredDateByRefreshToken(token)).thenReturn(validTime);

		// when
		reissueService.execute(token);

		// then
		assertAll(
				() -> assertTrue(invalidTokenRepository.isExistToken(token)),
				() -> verify(authenticationTokenGenerator).execute(memberId));
	}
}
