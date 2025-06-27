package com.blackcompany.eeos.auth.application.service;

import static org.mockito.Mockito.when;

import com.blackcompany.eeos.auth.application.domain.OauthMemberModel;
import com.blackcompany.eeos.auth.application.domain.OauthServerType;
import com.blackcompany.eeos.auth.application.dto.request.OAuthLoginRequestCommand;
import com.blackcompany.eeos.auth.application.model.AuthorityModel;
import com.blackcompany.eeos.auth.application.repository.AuthorityRepository;
import com.blackcompany.eeos.auth.application.support.AuthenticationTokenGenerator;
import com.blackcompany.eeos.auth.fixture.FakeAuthority;
import com.blackcompany.eeos.auth.fixture.FakeOauthMember;
import java.util.Set;
import java.util.stream.Collectors;
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
	@Mock AuthorityRepository authorityRepository;

	@InjectMocks AuthFacadeService authFacadeService;

	@Test
	@DisplayName("로그인 요청이 들어오면 토큰을 반환한다.")
	void response_token() {
		// given
		String type = "type";
		String authCode = "code";
		Long memberId = 1L;
		String uri = "uri";
		Set<AuthorityModel> authorities = Set.of(FakeAuthority.authorityModel(1L, memberId, "role"));
		Set<String> roles = authorities.stream().map(AuthorityModel::getName).collect(Collectors.toSet());

		OauthMemberModel oauthMemberModel =
				FakeOauthMember.oauthMemberModel(OauthServerType.SLACK, memberId);

		when(oauthClientService.getOauthMember(type, authCode, uri)).thenReturn(oauthMemberModel);
		when(authService.authenticate(oauthMemberModel)).thenReturn(memberId);
		when(authorityRepository.findByMemberId(memberId)).thenReturn(authorities);

		// when
		authFacadeService.login(new OAuthLoginRequestCommand(type, authCode, uri));



		// then
		Mockito.verify(authenticationTokenGenerator).execute(memberId, roles);
	}
}
