package com.blackcompany.eeos.member.application.model;

import static org.junit.jupiter.api.Assertions.*;

import com.blackcompany.eeos.member.application.exception.DeniedUpdateActiveException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ActiveStatusTest {

	@Test
	@DisplayName("존재하지 않은 활동 상태일 때 예외가 발생한다.")
	void not_found_active_status() {
		// given
		String status = "known";

		// when & then
		Assertions.assertThrows(DeniedUpdateActiveException.class, () -> ActiveStatus.find(status));
	}

	@Test
	@DisplayName("존재하는 활동 상태일 때 해당 활동 상태를 반환한다.")
	void found_active_status() {
		// given
		String status = "am";

		// when
		ActiveStatus activeStatus = ActiveStatus.find(status);

		// then
		assertEquals(ActiveStatus.AM, activeStatus);
	}

	@Test
	@DisplayName("주어진 활동 상태가 일치하면 true를 반환한다.")
	void true_valid_same_active_status() {
		// given
		String status = "am";

		// when
		boolean isSame = ActiveStatus.isSame(status, ActiveStatus.AM);

		// then
		assertEquals(true, isSame);
	}

	@Test
	@DisplayName("주어진 활동 상태가 일치하지 않으면 false를 반환한다.")
	void false_valid_same_active_status() {
		// given
		String status = "am";

		// when
		boolean isSame = ActiveStatus.isSame(status, ActiveStatus.RM);

		// then
		assertEquals(false, isSame);
	}
}
