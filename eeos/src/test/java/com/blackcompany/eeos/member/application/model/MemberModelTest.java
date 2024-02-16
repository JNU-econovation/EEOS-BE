package com.blackcompany.eeos.member.application.model;

import static org.junit.jupiter.api.Assertions.*;

import com.blackcompany.eeos.member.application.exception.DeniedUpdateActiveException;
import com.blackcompany.eeos.member.fixture.MemberFixture;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberModelTest {
	@Test
	@DisplayName("활동 상태를 변경한다.")
	void update_active_status() {
		// given
		MemberModel memberModel = MemberFixture.멤버_모델(1L, ActiveStatus.AM);

		// when
		MemberModel updatedMemberModel = memberModel.updateActiveStatus("rm");

		// then
		Assertions.assertEquals(ActiveStatus.RM.getStatus(), updatedMemberModel.getActiveStatus());
	}

	@Test
	@DisplayName("all 활동 상태로 변경할 때는 예외가 발생한다.")
	void fail_update_active_status() {
		// given
		String status = "all";
		MemberModel memberModel = MemberFixture.멤버_모델(1L, ActiveStatus.AM);

		// when & then
		assertThrows(DeniedUpdateActiveException.class, () -> memberModel.updateActiveStatus(status));
	}
}
