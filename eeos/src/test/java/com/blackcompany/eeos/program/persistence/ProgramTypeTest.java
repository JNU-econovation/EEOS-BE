package com.blackcompany.eeos.program.persistence;

import static org.junit.jupiter.api.Assertions.*;

import com.blackcompany.eeos.program.application.exception.NotFoundProgramTypeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProgramTypeTest {

	@Test
	@DisplayName("존재하지 않은 프로그램 타입일 때 예외가 발생한다.")
	void not_found_program_type() {
		// given
		String status = "known";

		// when & then
		Assertions.assertThrows(NotFoundProgramTypeException.class, () -> ProgramType.find(status));
	}

	@Test
	@DisplayName("존재하는 프로그램 타입일 때 해당 프로그램 타입을 반환한다.")
	void found_program_type() {
		// given
		String type = "demand";

		// when
		ProgramType programType = ProgramType.find(type);

		// then
		assertEquals(ProgramType.DEMAND, programType);
	}
}
