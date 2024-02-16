package com.blackcompany.eeos.program.persistence;

import static org.junit.jupiter.api.Assertions.*;

import com.blackcompany.eeos.program.application.exception.NotFoundProgramCategoryException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProgramCategoryTest {
	@Test
	@DisplayName("존재하지 않은 프로그램 카테고리일 때 예외가 발생한다.")
	void not_found_program_category() {
		// given
		String category = "known";

		// when & then
		Assertions.assertThrows(
				NotFoundProgramCategoryException.class, () -> ProgramCategory.find(category));
	}

	@Test
	@DisplayName("존재하는 프로그램 타입일 때 해당 프로그램 타입을 반환한다.")
	void found_program_category() {
		// given
		String category = "eventTeam";

		// when
		ProgramCategory programCategory = ProgramCategory.find(category);

		// then
		assertEquals(ProgramCategory.EVENT_TEAM, programCategory);
	}
}
