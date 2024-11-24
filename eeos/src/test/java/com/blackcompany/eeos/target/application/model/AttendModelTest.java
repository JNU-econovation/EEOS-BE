package com.blackcompany.eeos.target.application.model;

import static org.junit.jupiter.api.Assertions.*;

import com.blackcompany.eeos.target.application.exception.DeniedSaveAttendException;
import com.blackcompany.eeos.target.application.exception.NotSameBeforeAttendStatusException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AttendModelTest {

	@Test
	@DisplayName("이전 참석 정보가 변경전 참석정보와 일치하면 참석 상태를 변경한다.")
	void success_change_status() {
		// given
		AttendModel model = AttendModel.builder().status(AttendStatus.NONRESPONSE).build();

		// when
		AttendModel attendModel = model.changeStatus("attend");

		// then
		assertEquals(AttendStatus.ATTEND.getStatus(), attendModel.getStatus());
	}

	@Test
	@DisplayName("이전 참석 정보와 똑같은 참석 정보의 요청이 들어오면 에러가 발생한다.")
	void fail_change_status_when_noRelated() {
		// given
		AttendModel model = AttendModel.builder().status(AttendStatus.NONRELATED).build();

		// when & then
		assertThrows(NotSameBeforeAttendStatusException.class, () -> model.changeStatus("nonRelated"));
	}
}
