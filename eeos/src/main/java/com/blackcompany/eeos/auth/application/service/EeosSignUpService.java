package com.blackcompany.eeos.auth.application.service;

import com.blackcompany.eeos.auth.application.domain.OauthServerType;
import com.blackcompany.eeos.auth.application.domain.TokenModel;
import com.blackcompany.eeos.auth.application.dto.request.EeosSignUpCommand;
import com.blackcompany.eeos.auth.application.exception.DuplicateLoginIdException;
import com.blackcompany.eeos.auth.application.model.AccountModel;
import com.blackcompany.eeos.auth.application.model.AuthorityModel;
import com.blackcompany.eeos.auth.application.model.Role;
import com.blackcompany.eeos.auth.application.repository.AccountRepository;
import com.blackcompany.eeos.auth.application.repository.AuthorityRepository;
import com.blackcompany.eeos.auth.application.support.AuthenticationTokenGenerator;
import com.blackcompany.eeos.auth.application.support.EncryptHelper;
import com.blackcompany.eeos.auth.application.usecase.EeosSignUpUseCase;
import com.blackcompany.eeos.member.application.model.MemberModel;
import com.blackcompany.eeos.member.application.repository.MemberRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EeosSignUpService implements EeosSignUpUseCase {

	private final MemberRepository memberRepository;
	private final AccountRepository accountRepository;
	private final AuthorityRepository authorityRepository;
	private final EncryptHelper encryptHelper;
	private final AuthenticationTokenGenerator tokenGenerator;

	@Override
	@Transactional
	public TokenModel signUp(EeosSignUpCommand command) {
		validateDuplicateLoginId(command.getLoginId());

		MemberModel savedMember = saveMember(command.getName(), command.getGeneration());
		saveAccount(command.getLoginId(), command.getPassword(), savedMember.getMemberId());
		saveAuthority(savedMember.getMemberId());

		return tokenGenerator.execute(savedMember.getMemberId(), Set.of(Role.ROLE_USER.name()));
	}

	private void validateDuplicateLoginId(String loginId) {
		if (accountRepository.existsByLoginId(loginId)) {
			throw new DuplicateLoginIdException();
		}
	}

	private MemberModel saveMember(String name, Integer generation) {
		MemberModel memberModel =
				MemberModel.builder().name(name, generation).oauthServerType(OauthServerType.EEOS).build();
		return memberRepository.save(memberModel);
	}

	private void saveAccount(String loginId, String rawPassword, Long memberId) {
		String encryptedPassword = encryptHelper.encrypt(rawPassword);
		AccountModel accountModel =
				AccountModel.builder()
						.loginId(loginId)
						.password(encryptedPassword)
						.memberId(memberId)
						.build();
		try {
			accountRepository.save(accountModel);
		} catch (DataIntegrityViolationException e) {
			log.warn("loginId 중복 저장 시도 발생");
			throw new DuplicateLoginIdException();
		}
	}

	private void saveAuthority(Long memberId) {
		authorityRepository.save(AuthorityModel.create(memberId, Role.ROLE_USER));
	}
}
