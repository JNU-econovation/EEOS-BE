package com.blackcompany.eeos.program.application.model;

import static org.junit.jupiter.api.Assertions.*;

import com.blackcompany.eeos.common.utils.DateConverter;
import com.blackcompany.eeos.program.application.exception.DeniedProgramEditException;
import com.blackcompany.eeos.program.application.exception.NotAllowedUpdatedProgramAttendException;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProgramModelTest {

	@Test
	@DisplayName("프로그램 날짜가 현 날짜의 이후라면 상태는 진행중(active)이다.")
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
	@DisplayName("프로그램 날짜가 현 날짜와 같으면 상태는 진행중(active)이다.")
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
	@DisplayName("프로그램 날짜가 현 날짜의 이전이라면 상태는 완료(end)이다.")
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

	@Test
	@DisplayName("작성자 본인일 경우 프로그램 삭제가 가능하다.")
	void can_delete() {
		// given
		LocalDate date = LocalDate.now().minusDays(1L);

		ProgramModel model =
				ProgramModel.builder().programDate(DateConverter.toEpochSecond(date)).writer(1L).build();

		// when & then
		assertDoesNotThrow(() -> model.validateDelete(1L));
	}

	@Test
	@DisplayName("작성자 본인이 아닐 경우 프로그램 삭제가 불가능하다.")
	void cannot_delete() {
		// given
		LocalDate date = LocalDate.now().minusDays(1L);

		ProgramModel model =
				ProgramModel.builder().programDate(DateConverter.toEpochSecond(date)).writer(2L).build();

		// when & then
		assertThrows(DeniedProgramEditException.class, () -> model.validateDelete(1L));
	}

	@Test
	@DisplayName("완료(end)된 프로그램의 참석 대상자를 작성자는 수정할 수 있다.")
	void can_edit_when_end_program() {
		// given
		LocalDate date = LocalDate.now().minusDays(1L);

		ProgramModel model =
				ProgramModel.builder().programDate(DateConverter.toEpochSecond(date)).writer(2L).build();

		// when & then
		assertDoesNotThrow(() -> model.validateEditAttend(2L));
	}

	@Test
	@DisplayName("진행중(active)인 프로그램의 참석 대상자는 수정할 수 없다")
	void cannot_edit_when_active_program() {
		// given
		LocalDate date = LocalDate.now().plusDays(1L);

		ProgramModel model =
				ProgramModel.builder().programDate(DateConverter.toEpochSecond(date)).writer(2L).build();

		// when & then
		assertThrows(NotAllowedUpdatedProgramAttendException.class, () -> model.validateEditAttend(2L));
	}

	@Test
	@DisplayName("프로그램의 작성자가 아닐 시에 참석 대상자를 수정하지 못 한다.")
	void cannot_edit_when_not_writer() {
		// given
		LocalDate date = LocalDate.now().plusDays(1L);

		ProgramModel model =
				ProgramModel.builder().programDate(DateConverter.toEpochSecond(date)).writer(2L).build();

		// when & then
		assertThrows(DeniedProgramEditException.class, () -> model.validateEditAttend(1L));
	}
}
