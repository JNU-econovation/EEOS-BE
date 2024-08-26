package com.blackcompany.eeos.member.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.blackcompany.eeos.auth.persistence.OAuthMemberRepository;
import com.blackcompany.eeos.common.DataClearExtension;
import com.blackcompany.eeos.member.application.dto.ChangeActiveStatusRequest;
import com.blackcompany.eeos.member.application.dto.CommandMemberResponse;
import com.blackcompany.eeos.member.application.exception.DeniedMemberEditException;
import com.blackcompany.eeos.member.application.model.ActiveStatus;
import com.blackcompany.eeos.member.fixture.MemberFixture;
import com.blackcompany.eeos.member.persistence.MemberEntity;
import com.blackcompany.eeos.member.persistence.MemberRepository;
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
		MemberEntity adminEntity = MemberFixture.어드민_엔티티(adminId);
		MemberEntity memberEntity = MemberFixture.멤버_엔티티(memberId1, ActiveStatus.AM);

		memberRepository.save(adminEntity);
		memberRepository.save(memberEntity);

		ChangeActiveStatusRequest request =
				ChangeActiveStatusRequest.builder().activeStatus("rm").build();

		// when
		CommandMemberResponse result =
				adminMemberService.changeActiveStatus(adminEntity.getId(), memberEntity.getId(), request);

		// then
		assertEquals(ActiveStatus.RM.getStatus(), result.getActiveStatus());
		assertEquals(memberEntity.getName(), result.getName());
	}

	@Test
	@DisplayName("관리자가 아닌 사용자가 회원 상태를 변경하려고 하면 예외가 발생한다.")
	void non_admin_change_status() {
		// given
		MemberEntity nonAdminEntity = MemberFixture.멤버_엔티티(memberId1, ActiveStatus.AM);
		MemberEntity memberEntity = MemberFixture.멤버_엔티티(memberId2, ActiveStatus.AM);

		memberRepository.save(nonAdminEntity);
		memberRepository.save(memberEntity);

		ChangeActiveStatusRequest request =
				ChangeActiveStatusRequest.builder().activeStatus("rm").build();

		// when & then
		assertThrows(
				DeniedMemberEditException.class,
				() ->
						adminMemberService.changeActiveStatus(
								nonAdminEntity.getId(), memberEntity.getId(), request));
	}

	@Test
	@DisplayName("관리자는 회원을 삭제할 수 있다.")
	void admin_delete_member() {
		// given
		MemberEntity adminEntity = MemberFixture.어드민_엔티티(adminId);
		MemberEntity memberEntity = MemberFixture.멤버_엔티티(memberId1, ActiveStatus.AM);

		memberRepository.save(adminEntity);
		memberRepository.save(memberEntity);

		// when
		adminMemberService.delete(adminEntity.getId(), memberEntity.getId());

		// then
		assertFalse(memberRepository.existsById(memberEntity.getId()));
		assertFalse(oAuthMemberRepository.existsById(memberEntity.getId()));
	}

	@Test
	@DisplayName("관리자가 아닌 사용자가 회원을 삭제하려고하면 예외가 발생한다.")
	void non_admin_delete_member() {
		// given
		MemberEntity nonAdminEntity = MemberFixture.멤버_엔티티(memberId1, ActiveStatus.AM);
		MemberEntity memberEntity = MemberFixture.멤버_엔티티(memberId2, ActiveStatus.AM);

		memberRepository.save(nonAdminEntity);
		memberRepository.save(memberEntity);

		// when & then
		assertThrows(
				DeniedMemberEditException.class,
				() -> adminMemberService.delete(nonAdminEntity.getId(), memberEntity.getId()));
	}
}
