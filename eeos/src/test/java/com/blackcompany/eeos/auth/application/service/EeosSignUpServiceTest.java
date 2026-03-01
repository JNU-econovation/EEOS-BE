package com.blackcompany.eeos.auth.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.blackcompany.eeos.auth.application.domain.TokenModel;
import com.blackcompany.eeos.auth.application.dto.request.EeosSignUpCommand;
import com.blackcompany.eeos.auth.application.exception.DuplicateLoginIdException;
import com.blackcompany.eeos.auth.application.model.Role;
import com.blackcompany.eeos.auth.application.repository.AccountRepository;
import com.blackcompany.eeos.auth.application.repository.AuthorityRepository;
import com.blackcompany.eeos.auth.application.support.AuthenticationTokenGenerator;
import com.blackcompany.eeos.auth.application.support.EncryptHelper;
import com.blackcompany.eeos.member.application.model.MemberModel;
import com.blackcompany.eeos.member.application.repository.MemberRepository;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EeosSignUpServiceTest {

	@Mock MemberRepository memberRepository;
	@Mock AccountRepository accountRepository;
	@Mock AuthorityRepository authorityRepository;
	@Mock EncryptHelper encryptHelper;
	@Mock AuthenticationTokenGenerator tokenGenerator;

	@InjectMocks EeosSignUpService eeosSignUpService;

	@Test
	@DisplayName("정상적인 회원가입 요청이 들어오면 토큰을 반환한다.")
	void signUp_success() {
		// given
		Long memberId = 1L;
		String loginId = "testId";
		String rawPassword = "password123";
		String encryptedPassword = "encrypted_password123";
		String name = "홍길동";
		Integer generation = 15;

		EeosSignUpCommand command =
				new EeosSignUpCommand(loginId, rawPassword, generation, name, "am");

		MemberModel savedMember = MemberModel.builder().id(memberId).name(name, generation).build();
		TokenModel expectedToken =
				TokenModel.builder().accessToken("access_token").refreshToken("refresh_token").build();

		when(accountRepository.existsByLoginId(loginId)).thenReturn(false);
		when(encryptHelper.encrypt(rawPassword)).thenReturn(encryptedPassword);
		when(memberRepository.save(any(MemberModel.class))).thenReturn(savedMember);
		when(tokenGenerator.execute(memberId, Set.of(Role.ROLE_USER.name()))).thenReturn(expectedToken);

		// when
		TokenModel result = eeosSignUpService.signUp(command);

		// then
		assertEquals(expectedToken, result);
		verify(accountRepository).save(any());
		verify(authorityRepository).save(any());
		verify(tokenGenerator).execute(memberId, Set.of(Role.ROLE_USER.name()));
	}

	@Test
	@DisplayName("이미 사용 중인 아이디로 회원가입하면 DuplicateLoginIdException이 발생한다.")
	void signUp_duplicateLoginId_throwsException() {
		// given
		String loginId = "existingId";
		EeosSignUpCommand command = new EeosSignUpCommand(loginId, "password", 15, "홍길동", "am");

		when(accountRepository.existsByLoginId(loginId)).thenReturn(true);

		// when & then
		assertThrows(DuplicateLoginIdException.class, () -> eeosSignUpService.signUp(command));

		verify(memberRepository, never()).save(any());
		verify(accountRepository, never()).save(any());
		verify(tokenGenerator, never()).execute(any(), any());
	}
}
