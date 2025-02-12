package com.blackcompany.eeos.auth.application.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.blackcompany.eeos.auth.application.domain.OauthMemberModel;
import com.blackcompany.eeos.auth.application.domain.converter.OauthMemberEntityConverter;
import com.blackcompany.eeos.auth.application.repository.OAuthMemberRepository;
import com.blackcompany.eeos.auth.fixture.FakeOauthMember;
import com.blackcompany.eeos.member.application.model.ActiveStatus;
import com.blackcompany.eeos.member.application.model.MemberModel;
import com.blackcompany.eeos.member.application.model.converter.MemberEntityConverter;
import com.blackcompany.eeos.member.application.repository.MemberRepository;
import com.blackcompany.eeos.member.fixture.MemberFixture;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

	@Mock MemberRepository memberRepository;
	@Mock OAuthMemberRepository oAuthMemberRepository;
	@Spy MemberEntityConverter memberEntityConverter;
	@Spy OauthMemberEntityConverter oauthMemberEntityConverter;
	@InjectMocks AuthService authService;

	@Test
	@DisplayName("신규 회원인 경우 oauth에서 가져온 회원 정보를 저장한다.")
	void login_existing_user() {
		// given
		OauthMemberModel expectedModel = FakeOauthMember.oauthMemberModel();
		MemberModel savedMember = MemberFixture.멤버_모델(1L, ActiveStatus.AM);

		when(oAuthMemberRepository.findByOauthId(expectedModel.getOauthId()))
				.thenReturn(Optional.empty());
		when(memberRepository.save(any())).thenReturn(savedMember);
		when(oAuthMemberRepository.save(any())).thenReturn(expectedModel);

		// when
		authService.authenticate(expectedModel);

		// then
		assertAll(
				() -> verify(memberRepository).save(any()),
				() -> verify(oAuthMemberRepository).save(any()));
	}
}
