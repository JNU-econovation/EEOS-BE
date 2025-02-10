package com.blackcompany.eeos.member.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.blackcompany.eeos.auth.application.repository.OAuthMemberRepository;
import com.blackcompany.eeos.common.DataClearExtension;
import com.blackcompany.eeos.member.application.dto.ChangeActiveStatusRequest;
import com.blackcompany.eeos.member.application.dto.CommandMemberResponse;
import com.blackcompany.eeos.member.application.exception.DeniedMemberEditException;
import com.blackcompany.eeos.member.application.model.ActiveStatus;
import com.blackcompany.eeos.member.application.model.MemberModel;
import com.blackcompany.eeos.member.application.repository.MemberRepository;
import com.blackcompany.eeos.member.fixture.MemberFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

@SpringBootTest
@ExtendWith(DataClearExtension.class)
class AdminMemberServiceTest {

	@Autowired private AdminMemberService adminMemberService;

	@SpyBean private MemberRepository memberRepository;

	@MockBean private OAuthMemberRepository oAuthMemberRepository;

	Long adminId = 1L;
	Long memberId1 = 2L;
	Long memberId2 = 3L;

	@Test
	@DisplayName("관리자는 회원의 상태를 변경할 수 있다.")
	void admin_change_status() {
		// given
		MemberModel admin = MemberFixture.어드민_모델(adminId);
		MemberModel member = MemberFixture.멤버_모델(memberId1, ActiveStatus.AM);

		memberRepository.save(admin);
		memberRepository.save(member);

		ChangeActiveStatusRequest request =
				ChangeActiveStatusRequest.builder().activeStatus("rm").build();

		// when
		CommandMemberResponse result =
				adminMemberService.changeActiveStatus(admin.getId(), member.getId(), request);

		// then
		assertEquals(ActiveStatus.RM.getStatus(), result.getActiveStatus());
		assertEquals(member.getName(), result.getName());
	}

	@Test
	@DisplayName("관리자가 아닌 사용자가 회원 상태를 변경하려고 하면 예외가 발생한다.")
	void non_admin_change_status() {
		// given
		MemberModel nonAdmin = MemberFixture.멤버_모델(memberId1, ActiveStatus.AM);
		MemberModel member = MemberFixture.멤버_모델(memberId2, ActiveStatus.AM);

		memberRepository.save(nonAdmin);
		memberRepository.save(member);

		ChangeActiveStatusRequest request =
				ChangeActiveStatusRequest.builder().activeStatus("rm").build();

		// when & then
		assertThrows(
				DeniedMemberEditException.class,
				() -> adminMemberService.changeActiveStatus(nonAdmin.getId(), member.getId(), request));
	}

	@Test
	@DisplayName("관리자는 회원을 삭제할 수 있다.")
	void admin_delete_member() {
		// given
		MemberModel admin = MemberFixture.어드민_모델(adminId);
		MemberModel member = MemberFixture.멤버_모델(memberId1, ActiveStatus.AM);

		memberRepository.save(admin);
		memberRepository.save(member);

		// when
		adminMemberService.delete(admin.getId(), member.getId());

		// then
		assertFalse(memberRepository.existsById(member.getId()));
		assertFalse(oAuthMemberRepository.existsById(member.getId()));
	}

	@Test
	@DisplayName("관리자가 아닌 사용자가 회원을 삭제하려고하면 예외가 발생한다.")
	void non_admin_delete_member() {
		// given
		MemberModel nonAdmin = MemberFixture.멤버_모델(memberId1, ActiveStatus.AM);
		MemberModel member = MemberFixture.멤버_모델(memberId2, ActiveStatus.AM);

		memberRepository.save(nonAdmin);
		memberRepository.save(member);

		// when & then
		assertThrows(
				DeniedMemberEditException.class,
				() -> adminMemberService.delete(nonAdmin.getId(), member.getId()));
	}
}
