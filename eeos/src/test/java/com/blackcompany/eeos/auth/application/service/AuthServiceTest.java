package com.blackcompany.eeos.auth.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.blackcompany.eeos.auth.application.domain.OauthMemberModel;
import com.blackcompany.eeos.auth.application.domain.OauthServerType;
import com.blackcompany.eeos.auth.application.domain.converter.OauthMemberEntityConverter;
import com.blackcompany.eeos.auth.application.exception.OAuthSignupRestrictedException;
import com.blackcompany.eeos.auth.application.repository.AuthorityRepository;
import com.blackcompany.eeos.auth.application.repository.OAuthMemberRepository;
import com.blackcompany.eeos.auth.fixture.FakeOauthMember;
import com.blackcompany.eeos.member.application.model.MemberModel;
import com.blackcompany.eeos.member.application.model.converter.MemberEntityConverter;
import com.blackcompany.eeos.member.application.repository.MemberRepository;
import java.util.Optional;
import org.junit.jupiter.api.Disabled;
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
	@Mock AuthorityRepository authorityRepository;
	@Spy MemberEntityConverter memberEntityConverter;
	@Spy OauthMemberEntityConverter oauthMemberEntityConverter;
	@InjectMocks AuthService authService;

	@Test
	@DisplayName("슬랙을 통한 신규 회원가입은 불가능하다.")
	@Disabled
	void sign_up_slack_user() {
		// given
		OauthMemberModel slackOAuthMember = FakeOauthMember.oauthMemberModel(OauthServerType.SLACK);

		when(oAuthMemberRepository.findByOauthId(slackOAuthMember.getOauthId()))
				.thenReturn(Optional.empty());

		// when & then
		assertThrows(
				OAuthSignupRestrictedException.class,
				() -> {
					authService.authenticate(slackOAuthMember);
				});
	}

	@Test
	@DisplayName("슬랙을 통한 로그인은 가능하다.")
	void login_slack_user() {
		// given
		OauthMemberModel slackOAuthMember = FakeOauthMember.oauthMemberModel(OauthServerType.SLACK, 1L);
		when(oAuthMemberRepository.findByOauthId(slackOAuthMember.getOauthId()))
				.thenReturn(Optional.of(slackOAuthMember));

		// when
		Long memberId = authService.authenticate(slackOAuthMember);

		// then
		assertEquals(memberId, 1L);
	}

	@Test
	@DisplayName("깃허브를 통한 신규 회원가입은 가능하다.")
	void sign_up_github_user() {
		// given
		OauthMemberModel githubOAuthMember = FakeOauthMember.oauthMemberModel(OauthServerType.GITHUB);
		MemberModel savedMember =
				MemberModel.builder()
						.id(1L)
						.name(githubOAuthMember.getName())
						.oauthServerType(OauthServerType.GITHUB)
						.build();

		OauthMemberModel savedOAuthMember =
				githubOAuthMember.toBuilder().memberId(savedMember.getId()).build();

		when(oAuthMemberRepository.findByOauthId(githubOAuthMember.getOauthId()))
				.thenReturn(Optional.empty());
		when(memberRepository.save(any(MemberModel.class))).thenReturn(savedMember);
		when(oAuthMemberRepository.save(any(OauthMemberModel.class))).thenReturn(savedOAuthMember);

		// when
		Long authenticatedMemberId = authService.authenticate(githubOAuthMember);

		// then
		assertEquals(authenticatedMemberId, savedMember.getId());
	}

	@Test
	@DisplayName("깃허브를 통한 로그인은 가능하다.")
	void login_github_user() {
		// given
		OauthMemberModel slackOAuthMember =
				FakeOauthMember.oauthMemberModel(OauthServerType.GITHUB, 1L);
		when(oAuthMemberRepository.findByOauthId(slackOAuthMember.getOauthId()))
				.thenReturn(Optional.of(slackOAuthMember));

		// when
		Long memberId = authService.authenticate(slackOAuthMember);

		// then
		assertEquals(memberId, 1L);
	}
}
