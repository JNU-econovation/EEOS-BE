package com.blackcompany.eeos.member.application.service;

import com.blackcompany.eeos.auth.application.domain.converter.OauthMemberEntityConverter;
import com.blackcompany.eeos.auth.application.model.AccountEntityConverter;
import com.blackcompany.eeos.auth.application.model.AccountModel;
import com.blackcompany.eeos.auth.persistence.AccountEntity;
import com.blackcompany.eeos.auth.persistence.AccountRepository;
import com.blackcompany.eeos.auth.persistence.OAuthMemberEntity;
import com.blackcompany.eeos.auth.persistence.OAuthMemberRepository;
import com.blackcompany.eeos.member.application.model.AdminInfo;
import com.blackcompany.eeos.member.application.model.MemberModel;
import com.blackcompany.eeos.member.application.model.converter.MemberEntityConverter;
import com.blackcompany.eeos.member.application.usecase.CreateAdminMemberUsecase;
import com.blackcompany.eeos.member.persistence.MemberEntity;
import com.blackcompany.eeos.member.persistence.MemberRepository;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
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
		MemberEntity savedMember = saveMember(memberEntityConverter.toEntity(createMember()));

		saveAccount(accountEntityConverter.toEntity(createAccount(savedMember.getId())));
		saveOauthMember(oauthMemberEntityConverter.toEntity(savedMember.getId()));

		return savedMember.getId();
	}

	@Override
	public boolean isExist() {
		List<MemberEntity> members =
				memberRepository.findMembers().stream()
						.filter(MemberEntity::isAdmin)
						.collect(Collectors.toList());

		return !members.isEmpty() || !findAdminAccount() || !findAdminOauthMember();
	}

	private MemberEntity saveMember(MemberEntity entity) {
		return memberRepository.save(entity);
	}

	private AccountEntity saveAccount(AccountEntity entity) {
		return accountRepository.save(entity);
	}

	private OAuthMemberEntity saveOauthMember(OAuthMemberEntity entity) {
		return oAuthMemberRepository.save(entity);
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
