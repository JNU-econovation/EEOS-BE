package com.blackcompany.eeos.auth.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.blackcompany.eeos.auth.application.domain.token.TokenResolver;
import com.blackcompany.eeos.auth.persistence.InvalidTokenRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class DeactivateMemberServiceTest {

	@Mock InvalidTokenRepository invalidTokenRepository;
	@Mock TokenResolver tokenResolver;
	@Mock ApplicationEventPublisher eventPublisher;

	@InjectMocks DeactivateMemberService deactivateMemberService;

	@Test
	@DisplayName("로그아웃 시 리프레시 토큰을 블랙리스트에 등록한다.")
	void logOut_saves_token_to_blacklist() {
		// given
		String token = "refresh-token";
		Long memberId = 1L;
		Long expiredTime = System.currentTimeMillis() + 60000L;

		when(tokenResolver.getExpiredDateByRefreshToken(token)).thenReturn(expiredTime);

		// when
		deactivateMemberService.logOut(token, memberId);

		// then
		verify(invalidTokenRepository).save(token, memberId, expiredTime);
	}
}
