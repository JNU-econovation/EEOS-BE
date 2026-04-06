package com.blackcompany.eeos.member.application.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.blackcompany.eeos.auth.application.domain.OauthServerType;
import com.blackcompany.eeos.auth.application.model.AccountModel;
import com.blackcompany.eeos.auth.application.repository.AccountRepository;
import com.blackcompany.eeos.common.DataClearExtension;
import com.blackcompany.eeos.member.application.model.ActiveStatus;
import com.blackcompany.eeos.member.application.model.MemberModel;
import com.blackcompany.eeos.member.application.repository.MemberRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ExtendWith(DataClearExtension.class)
class MemberRepositoryTest {

	@Autowired private MemberRepository memberRepository;
	@Autowired private AccountRepository accountRepository;

	@Test
	@DisplayName("Account가 없는 Slack OAuth 회원은 Slack Only 회원 목록에 조회된다.")
	void findSlackOnlyMembers_slackMemberWithoutAccount_isIncluded() {
		// given
		MemberModel slackOnlyMember =
				MemberModel.builder()
						.name("슬랙전용회원")
						.oauthServerType(OauthServerType.SLACK)
						.activeStatus(ActiveStatus.AM)
						.isAdmin(false)
						.build();
		MemberModel saved = memberRepository.save(slackOnlyMember);

		// when
		List<MemberModel> result = memberRepository.findSlackOnlyMembers();

		// then
		assertThat(result).extracting(MemberModel::getId).contains(saved.getId());
	}

	@Test
	@DisplayName("Account가 있는 Slack OAuth 회원은 Slack Only 회원 목록에 조회되지 않는다.")
	void findSlackOnlyMembers_slackMemberWithAccount_isExcluded() {
		// given
		MemberModel slackMemberWithAccount =
				MemberModel.builder()
						.name("계정있는슬랙회원")
						.oauthServerType(OauthServerType.SLACK)
						.activeStatus(ActiveStatus.AM)
						.isAdmin(false)
						.build();
		MemberModel saved = memberRepository.save(slackMemberWithAccount);

		// Account를 직접 저장하여 해당 Member에 Account가 연결된 상태를 만든다
		AccountModel account =
				AccountModel.builder()
						.memberId(saved.getId())
						.loginId("linked_user_" + saved.getId())
						.password("encrypted_pw")
						.build();
		accountRepository.save(account);

		// when
		List<MemberModel> result = memberRepository.findSlackOnlyMembers();

		// then
		assertThat(result).extracting(MemberModel::getId).doesNotContain(saved.getId());
	}

	@Test
	@DisplayName("Account가 없는 일반(non-Slack) 회원은 Slack Only 회원 목록에 조회되지 않는다.")
	void findSlackOnlyMembers_nonSlackMemberWithoutAccount_isExcluded() {
		// given
		MemberModel eeosOnlyMember =
				MemberModel.builder()
						.name("이오스전용회원")
						.oauthServerType(OauthServerType.EEOS)
						.activeStatus(ActiveStatus.AM)
						.isAdmin(false)
						.build();
		MemberModel saved = memberRepository.save(eeosOnlyMember);

		// when
		List<MemberModel> result = memberRepository.findSlackOnlyMembers();

		// then
		assertThat(result).extracting(MemberModel::getId).doesNotContain(saved.getId());
	}
}
