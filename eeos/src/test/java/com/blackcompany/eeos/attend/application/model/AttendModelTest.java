package com.blackcompany.eeos.attend.application.model;

import static org.junit.jupiter.api.Assertions.*;

import com.blackcompany.eeos.attend.application.exception.NotSameBeforeAttendStatusException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AttendModelTest {

	@Test
	@DisplayName("다른 참석 상태일 때 예외가 발생한다.")
	void differentAttendStatus() {
		// given
		AttendModel model = AttendModel.builder().status(AttendStatus.NONRESPONSE).build();

		// when & then
		Assertions.assertThrows(
				NotSameBeforeAttendStatusException.class, () -> model.validateSame("attend"));
	}

	@Test
	@DisplayName("같은 참석 상태일 때 예외가 발생하지 않는다.")
	void validateSame() {
		// given
		AttendModel model = AttendModel.builder().status(AttendStatus.NONRESPONSE).build();

		// when & then
		Assertions.assertDoesNotThrow(() -> model.validateSame("attend"));
	}

	@Test
	@DisplayName("참석 상태를 변경한다.")
	void changeStatus() {
		// given
		AttendModel model = AttendModel.builder().status(AttendStatus.NONRESPONSE).build();

		// when
		model.changeStatus("attend");

		// then
		assertEquals(AttendStatus.ATTEND, model.getStatus());
	}
}
