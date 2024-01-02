package com.blackcompany.eeos.auth.application.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.blackcompany.eeos.auth.application.domain.converter.TokenModelConverter;
import com.blackcompany.eeos.auth.application.domain.token.TokenProvider;
import com.blackcompany.eeos.auth.application.domain.token.TokenResolver;
import com.blackcompany.eeos.auth.persistence.AuthInfoEntity;
import com.blackcompany.eeos.auth.persistence.AuthInfoEntityConverter;
import com.blackcompany.eeos.auth.persistence.AuthInfoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateTokenServiceTest {
	@InjectMocks CreateTokenService createTokenService;
	@Mock TokenResolver tokenResolver;
	@Mock
	TokenModelConverter tokenModelConverter;

	@Mock TokenProvider tokenProvider;
	@Mock AuthInfoRepository authInfoRepository;
	@Mock AuthInfoEntityConverter authInfoEntityConverter;

	@Test
	@DisplayName("토큰 생성 요청이 들어오면 토큰을 생성한 후 토큰을 저장한다.")
	void save_token_when_request_create_token() {
		// given
		Long userId = 1L;
		String accessToken = "mocked_access_token";
		String refreshToken = "mocked_refresh_token";

		AuthInfoEntity entity =
				AuthInfoEntity.builder().id(1L).memberId(userId).token(refreshToken).build();

		when(tokenProvider.createAccessToken(userId)).thenReturn(accessToken);
		when(tokenProvider.createRefreshToken(userId)).thenReturn(refreshToken);
		when(authInfoEntityConverter.from(userId, refreshToken)).thenReturn(entity);

		// when
		createTokenService.execute(userId);

		// then
		verify(authInfoRepository).save(entity);
	}
}
