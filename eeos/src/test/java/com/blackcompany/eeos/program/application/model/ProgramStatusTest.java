package com.blackcompany.eeos.program.application.model;

import static org.junit.jupiter.api.Assertions.*;

import com.blackcompany.eeos.program.application.exception.NotFoundProgramStatusException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProgramStatusTest {

	@Test
	@DisplayName("존재하는 status라면 ProgramStatus를 반환한다.")
	void getStatusSuccess() {
		// given
		ProgramStatus active = ProgramStatus.getStatus("active");

		// when
		assertEquals(ProgramStatus.ACTIVE, active);
	}

	@Test
	@DisplayName("존재하지 않은 status라면 예외를 발생시킨다.")
	void getStatusFail() {
		// when & then
		assertThrows(NotFoundProgramStatusException.class, () -> ProgramStatus.getStatus("zero"));
	}
}
