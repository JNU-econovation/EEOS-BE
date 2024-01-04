package com.blackcompany.eeos.member.application.model;

import static org.junit.jupiter.api.Assertions.*;

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
		MemberModel memberModel = FakeMember.AmMemberModel();

		// when
		MemberModel updatedMemberModel = memberModel.updateActiveStatus(status);

		// then
		Assertions.assertEquals(ActiveStatus.RM, updatedMemberModel.getActiveStatus());
	}
}
