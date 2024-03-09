package com.blackcompany.eeos.auth.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.blackcompany.eeos.auth.application.domain.OauthMemberModel;
import com.blackcompany.eeos.auth.application.support.AuthenticationTokenGenerator;
import com.blackcompany.eeos.auth.fixture.FakeOauthMember;
import com.blackcompany.eeos.auth.persistence.OAuthMemberEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthFacadeServiceTest {
	@Mock OauthClientService oauthClientService;
	@Mock AuthenticationTokenGenerator authenticationTokenGenerator;
	@Mock AuthService authService;

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
		OAuthMemberEntity oAuthMemberEntity = FakeOauthMember.oauthInfoEntity();

		when(oauthClientService.getOauthMember(type, authCode, uri)).thenReturn(oauthMemberModel);
		when(authService.authenticate(oauthMemberModel)).thenReturn(oAuthMemberEntity);

		// when
		authFacadeService.login(type, authCode, uri);

		// then
		Mockito.verify(authenticationTokenGenerator).execute(memberId);
	}
}
