package com.blackcompany.eeos.attend.application.model;

import static org.junit.jupiter.api.Assertions.*;

import com.blackcompany.eeos.attend.application.exception.NotFoundAttendStatusException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AttendStatusTest {

	@Test
	@DisplayName("주어진 상태가 존재하는 활동상태라면 해당 활동상태를 반환한다.")
	void find_attend_status() {
		// given
		String status = "attend";

		// when
		AttendStatus attendStatus = AttendStatus.find(status);

		// then
		Assertions.assertEquals(AttendStatus.ATTEND, attendStatus);
	}

	@Test
	@DisplayName("주어진 상태가 존재하지 않는 활동상태라면 예외가 발생한다.")
	void fail_attend_status() {
		// given
		String status = "not_found_status";

		// when & then
		Assertions.assertThrows(NotFoundAttendStatusException.class, () -> AttendStatus.find(status));
	}

	@Test
	@DisplayName("주어진 활동상태가 동일하다면 true를 반환한다.")
	void is_same() {
		// given
		String source = "attend";
		AttendStatus targetStatus = AttendStatus.ATTEND;

		// when
		Assertions.assertEquals(true, AttendStatus.isSame(source, targetStatus));
	}

	@Test
	@DisplayName("주어진 활동상태가 동일하지 않다면 false를 반환한다.")
	void is_not_same() {
		// given
		String source = "absent";
		AttendStatus targetStatus = AttendStatus.ATTEND;

		// when
		Assertions.assertEquals(false, AttendStatus.isSame(source, targetStatus));
	}
}
