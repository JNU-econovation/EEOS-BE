package com.blackcompany.eeos.auth.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.blackcompany.eeos.auth.application.domain.OauthMemberModel;
import com.blackcompany.eeos.auth.application.domain.client.OauthMemberClientComposite;
import com.blackcompany.eeos.auth.fixture.FakeOauthMember;
import com.blackcompany.eeos.auth.persistence.OauthInfoEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthFacadeServiceTest {
	@Mock CreateTokenService createTokenService;

	@Mock AuthService authService;
	@Mock OauthClientService oauthClientService;

	@InjectMocks AuthFacadeService authFacadeService;

	@Test
	@DisplayName("로그인 요청이 들어오면 토큰을 반환한다.")
	void response_token() {
		// given
		String type = "type";
		String authCode = "code";
		Long memberId = 1L;
		String uri = "uri";

		OauthMemberModel oauthMemberModel = FakeOauthMember.oauthMemberModel();
		OauthInfoEntity oauthInfoEntity = FakeOauthMember.oauthInfoEntity();

		when(oauthClientService.getOauthMember(type, authCode, uri)).thenReturn(oauthMemberModel);
		when(authService.login(oauthMemberModel)).thenReturn(oauthInfoEntity);

		// when
		authFacadeService.login(type, authCode, uri);

		// then
		Mockito.verify(createTokenService).execute(memberId);
	}
}
