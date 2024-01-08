package com.blackcompany.eeos.member.application.model;

import static org.junit.jupiter.api.Assertions.*;

import com.blackcompany.eeos.member.application.exception.DeniedUpdateActiveException;
import com.blackcompany.eeos.member.fixture.FakeMember;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberModelTest {
	@Test
	@DisplayName("활동 상태를 변경한다.")
	void update_active_status() {
		// given
		String status = "rm";
		MemberModel memberModel = FakeMember.AmMandoMemberModel();

		// when
		MemberModel updatedMemberModel = memberModel.updateActiveStatus(status);

		// then
		Assertions.assertEquals(ActiveStatus.RM, updatedMemberModel.getActiveStatus());
	}

	@Test
	@DisplayName("all 활동 상태로 변경할 때는 예외가 발생한다.")
	void fail_update_active_status() {
		// given
		String status = "all";
		MemberModel memberModel = FakeMember.AmBadaMemberModel();

		// when & then
		assertThrows(DeniedUpdateActiveException.class, () -> memberModel.updateActiveStatus(status));
	}
}
