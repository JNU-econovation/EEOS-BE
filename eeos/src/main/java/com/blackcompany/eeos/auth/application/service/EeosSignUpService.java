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
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

		Set<String> roles = getRoles(savedMember.getMemberId());
		return tokenGenerator.execute(savedMember.getMemberId(), roles);
	}

	private void validateDuplicateLoginId(String loginId) {
		if (accountRepository.existsByLoginId(loginId)) {
			throw new DuplicateLoginIdException();
		}
	}

	private MemberModel saveMember(String name, Integer generation) {
		MemberModel memberModel =
				MemberModel.builder()
						.name(name, generation)
						.oauthServerType(OauthServerType.EEOS)
						.build();
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
		accountRepository.save(accountModel);
	}

	private void saveAuthority(Long memberId) {
		authorityRepository.save(AuthorityModel.create(memberId, Role.ROLE_USER));
	}

	private Set<String> getRoles(Long memberId) {
		return authorityRepository.findByMemberId(memberId).stream()
				.map(AuthorityModel::getRole)
				.map(Role::name)
				.collect(Collectors.toSet());
	}
}
