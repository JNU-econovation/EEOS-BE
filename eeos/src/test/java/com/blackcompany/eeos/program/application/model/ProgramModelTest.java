package com.blackcompany.eeos.program.application.model;

import static org.junit.jupiter.api.Assertions.*;

import com.blackcompany.eeos.common.utils.DateConverter;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProgramModelTest {

	@Test
	@DisplayName("프로그램 날짜가 현 날짜의 이후라면 상태는 active이다.")
	void calculateStatusAfterDay() {
		// given
		LocalDate date = LocalDate.now().plusDays(1L);

		ProgramModel model =
				ProgramModel.builder().programDate(DateConverter.toEpochSecond(date)).build();

		// when
		ProgramStatus status = model.calculate();

		// then
		assertEquals(ProgramStatus.ACTIVE, status);
	}

	@Test
	@DisplayName("프로그램 날짜가 현 날짜와 같으면 상태는 active이다.")
	void calculateStatusDay() {
		// given
		LocalDate date = LocalDate.now();

		ProgramModel model =
				ProgramModel.builder().programDate(DateConverter.toEpochSecond(date)).build();

		// when
		ProgramStatus status = model.calculate();

		// then
		assertEquals(ProgramStatus.ACTIVE, status);
	}

	@Test
	@DisplayName("프로그램 날짜가 현 날짜의 이전이라면 상태는 end이다.")
	void calculateStatusBeforeDay() {
		// given
		LocalDate date = LocalDate.now().minusDays(1L);

		ProgramModel model =
				ProgramModel.builder().programDate(DateConverter.toEpochSecond(date)).build();

		// when
		ProgramStatus status = model.calculate();

		// then
		assertEquals(ProgramStatus.END, status);
	}
}
