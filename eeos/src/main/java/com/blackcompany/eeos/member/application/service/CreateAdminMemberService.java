package com.blackcompany.eeos.member.application.service;

import com.blackcompany.eeos.auth.application.domain.converter.OauthMemberEntityConverter;
import com.blackcompany.eeos.auth.application.model.AccountEntityConverter;
import com.blackcompany.eeos.auth.application.model.AccountModel;
import com.blackcompany.eeos.auth.application.repository.OAuthMemberRepository;
import com.blackcompany.eeos.auth.persistence.AccountEntity;
import com.blackcompany.eeos.auth.persistence.AccountRepository;
import com.blackcompany.eeos.member.application.model.AdminInfo;
import com.blackcompany.eeos.member.application.model.MemberModel;
import com.blackcompany.eeos.member.application.model.converter.MemberEntityConverter;
import com.blackcompany.eeos.member.application.repository.MemberRepository;
import com.blackcompany.eeos.member.application.usecase.CreateAdminMemberUsecase;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateAdminMemberService implements CreateAdminMemberUsecase {

	private final AdminInfo adminInfo;
	private final MemberRepository memberRepository;
	private final AccountRepository accountRepository;
	private final OAuthMemberRepository oAuthMemberRepository;
	private final MemberEntityConverter memberEntityConverter;
	private final AccountEntityConverter accountEntityConverter;
	private final OauthMemberEntityConverter oauthMemberEntityConverter;

	@PostConstruct
	public void init() {
		if (!isExist()) {
			log.info("관리자를 생성합니다.");
			log.info("생성된 관리자 ID : " + create());
		}
	}

	@Transactional
	@Override
	public Long create() {
		MemberModel savedMember = memberRepository.save(createMember());

		saveAccount(accountEntityConverter.toEntity(createAccount(savedMember.getId())));

		return savedMember.getId();
	}

	@Override
	public boolean isExist() {
		List<MemberModel> members =
				memberRepository.findMembers().stream().filter(MemberModel::isAdmin).toList();

		return !members.isEmpty() || !findAdminAccount() || !findAdminOauthMember();
	}

	private AccountEntity saveAccount(AccountEntity entity) {
		return accountRepository.save(entity);
	}

	private MemberModel createMember() {
		return MemberModel.builder()
				.name(adminInfo.getName())
				.isAdmin(true)
				.activeStatus(adminInfo.getActiveStatus())
				.oauthServerType(adminInfo.getOauthServerType())
				.build();
	}

	private AccountModel createAccount(Long memberId) {
		return AccountModel.builder()
				.memberId(memberId)
				.password(adminInfo.getPassword())
				.loginId(adminInfo.getLoginId())
				.build();
	}

	private boolean findAdminAccount() {
		return accountRepository.findByLoginId(adminInfo.getLoginId()).isEmpty();
	}

	private boolean findAdminOauthMember() {
		return oAuthMemberRepository.findByAccount(adminInfo.getLoginId()).isEmpty();
	}
}
