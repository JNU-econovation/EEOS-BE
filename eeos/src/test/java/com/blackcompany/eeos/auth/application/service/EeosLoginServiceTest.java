package com.blackcompany.eeos.auth.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.blackcompany.eeos.auth.application.domain.OauthServerType;
import com.blackcompany.eeos.auth.application.exception.NotFoundAccountException;
import com.blackcompany.eeos.auth.application.model.AccountModel;
import com.blackcompany.eeos.auth.application.repository.AccountRepository;
import com.blackcompany.eeos.auth.application.repository.AuthorityRepository;
import com.blackcompany.eeos.auth.application.repository.OAuthMemberRepository;
import com.blackcompany.eeos.auth.application.support.EncryptHelper;
import com.blackcompany.eeos.member.application.model.MemberModel;
import com.blackcompany.eeos.member.application.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EeosLoginServiceTest {

	@Mock MemberRepository memberRepository;
	@Mock OAuthMemberRepository oAuthMemberRepository;
	@Mock AccountRepository accountRepository;
	@Mock AuthorityRepository authorityRepository;
	@Mock EncryptHelper encryptHelper;

	@InjectMocks AuthService authService;

	@Test
	@DisplayName("올바른 id/password로 로그인하면 MemberModel을 반환한다.")
	void login_with_valid_credentials() {
		// given
		Long memberId = 1L;
		String loginId = "testId";
		String rawPassword = "password123";
		String encryptedPassword = "encrypted_password123";

		AccountModel accountModel =
				AccountModel.builder()
						.id(1L)
						.loginId(loginId)
						.password(encryptedPassword)
						.memberId(memberId)
						.build();

		MemberModel memberModel =
				MemberModel.builder()
						.id(memberId)
						.name("15기 홍길동")
						.oauthServerType(OauthServerType.EEOS)
						.build();

		when(accountRepository.findByLoginId(loginId)).thenReturn(accountModel);
		when(encryptHelper.isMatch(rawPassword, encryptedPassword)).thenReturn(true);
		when(memberRepository.findById(memberId)).thenReturn(memberModel);

		// when
		MemberModel result = authService.authenticate(loginId, rawPassword);

		// then
		assertEquals(memberId, result.getMemberId());
	}

	@Test
	@DisplayName("비밀번호가 틀리면 NotFoundAccountException이 발생한다.")
	void login_with_wrong_password_throws_exception() {
		// given
		String loginId = "testId";
		String rawPassword = "wrongPassword";
		String encryptedPassword = "encrypted_password123";

		AccountModel accountModel =
				AccountModel.builder()
						.id(1L)
						.loginId(loginId)
						.password(encryptedPassword)
						.memberId(1L)
						.build();

		when(accountRepository.findByLoginId(loginId)).thenReturn(accountModel);
		when(encryptHelper.isMatch(rawPassword, encryptedPassword)).thenReturn(false);

		// when & then
		assertThrows(NotFoundAccountException.class, () -> authService.authenticate(loginId, rawPassword));
	}

	@Test
	@DisplayName("존재하지 않는 id로 로그인하면 NotFoundAccountException이 발생한다.")
	void login_with_nonexistent_id_throws_exception() {
		// given
		String loginId = "nonExistentId";
		String rawPassword = "password123";

		when(accountRepository.findByLoginId(loginId)).thenThrow(new NotFoundAccountException());

		// when & then
		assertThrows(NotFoundAccountException.class, () -> authService.authenticate(loginId, rawPassword));
	}
}
