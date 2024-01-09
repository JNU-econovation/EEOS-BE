package com.blackcompany.eeos.program.application.model;

import static org.junit.jupiter.api.Assertions.*;

import com.blackcompany.eeos.common.utils.DateConverter;
import com.blackcompany.eeos.program.application.exception.DeniedProgramEditException;
import com.blackcompany.eeos.program.application.exception.NotAllowedUpdatedProgramAttendException;
import com.blackcompany.eeos.program.application.exception.NotAllowedUpdatedProgramTypeException;
import com.blackcompany.eeos.program.application.exception.NotFoundProgramCategoryException;
import com.blackcompany.eeos.program.application.exception.OverDateException;
import com.blackcompany.eeos.program.persistence.ProgramCategory;
import com.blackcompany.eeos.program.persistence.ProgramType;
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
		String programStatus = model.getProgramStatus();

		// then
		assertEquals(ProgramStatus.ACTIVE.getStatus(), programStatus);
	}

	@Test
	@DisplayName("프로그램 날짜가 현 날짜와 같으면 상태는 진행중(active)이다.")
	void calculateStatusDay() {
		// given
		LocalDate date = LocalDate.now();

		ProgramModel model =
				ProgramModel.builder().programDate(DateConverter.toEpochSecond(date)).build();

		// when
		String prgramStatus = model.getProgramStatus();

		// then
		assertEquals(ProgramStatus.ACTIVE.getStatus(), prgramStatus);
	}

	@Test
	@DisplayName("프로그램 날짜가 현 날짜의 이전이라면 상태는 완료(end)이다.")
	void calculateStatusBeforeDay() {
		// given
		LocalDate date = LocalDate.now().minusDays(1L);

		ProgramModel model =
				ProgramModel.builder().programDate(DateConverter.toEpochSecond(date)).build();

		// when
		String programStatus = model.getProgramStatus();

		// then
		assertEquals(ProgramStatus.END.getStatus(), programStatus);
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

	@Test
	@DisplayName("프로그램 타입은 수정하지 못 한다.")
	void cannot_edit_program_type() {
		// given
		LocalDate date = LocalDate.now().plusDays(1L);
		ProgramModel model =
				ProgramModel.builder()
						.programDate(DateConverter.toEpochSecond(date))
						.writer(2L)
						.programType(ProgramType.NOTIFICATION)
						.build();

		ProgramModel requestModel =
				ProgramModel.builder()
						.programDate(DateConverter.toEpochSecond(date))
						.writer(2L)
						.programType(ProgramType.DEMAND)
						.build();

		// when & then
		assertThrows(NotAllowedUpdatedProgramTypeException.class, () -> model.update(requestModel));
	}

	@Test
	@DisplayName("all로 프로그램 카테고리를 수정하지 못 한다.")
	// given
	void cannot_edit_program_category_all() {
		LocalDate date = LocalDate.now().plusDays(1L);
		ProgramModel model =
				ProgramModel.builder()
						.writer(1L)
						.programType(ProgramType.DEMAND)
						.programCategory(ProgramCategory.EVENT_TEAM)
						.build();

		ProgramModel requestModel =
				ProgramModel.builder()
						.writer(1L)
						.programType(ProgramType.DEMAND)
						.programCategory(ProgramCategory.ALL)
						.build();

		// when & then
		assertThrows(NotFoundProgramCategoryException.class, () -> model.update(requestModel));
	}

	@Test
	@DisplayName("프로그램 작성자가 아니면 프로그램은 수정하지 못 한다.")
	void cannot_edit_program_when_not_writer() {
		// given
		LocalDate date = LocalDate.now().plusDays(1L);
		ProgramModel model =
				ProgramModel.builder().programDate(DateConverter.toEpochSecond(date)).writer(2L).build();

		ProgramModel requestModel =
				ProgramModel.builder().programDate(DateConverter.toEpochSecond(date)).writer(1L).build();

		// when & then
		assertThrows(DeniedProgramEditException.class, () -> model.update(requestModel));
	}

	@Test
	@DisplayName("프로그램 작성자이며 프로그램 타입을 수정하지 않을 떄는 프로그램 수정이 가능하다.")
	void cannot_edit_program() {
		// given
		LocalDate date = LocalDate.now().plusDays(1L);
		ProgramModel model =
				ProgramModel.builder()
						.title("title")
						.content("content")
						.programDate(DateConverter.toEpochSecond(date))
						.programCategory(ProgramCategory.WEEKLY)
						.programType(ProgramType.DEMAND)
						.writer(2L)
						.build();

		ProgramModel requestModel =
				ProgramModel.builder()
						.title("title 변경")
						.content("content 변경")
						.programDate(DateConverter.toEpochSecond(date))
						.programCategory(ProgramCategory.EVENT_TEAM)
						.programType(ProgramType.DEMAND)
						.writer(2L)
						.build();

		// when
		ProgramModel update = model.update(requestModel);

		// then
		assertAll(
				() -> assertEquals(update.getProgramCategory(), model.getProgramCategory()),
				() -> assertEquals(update.getProgramType(), model.getProgramType()),
				() -> assertEquals(update.getTitle(), model.getTitle()),
				() -> assertEquals(update.getContent(), model.getContent()),
				() -> assertEquals(update.getProgramDate(), model.getProgramDate()));
	}

	@Test
	@DisplayName("프로그램 수정은 수정 기준 이전 날짜도 가능하다.")
	void can_edit_program_before_date() {
		// given
		LocalDate date = LocalDate.now().plusDays(1L);
		ProgramModel model =
				ProgramModel.builder()
						.title("title")
						.content("content")
						.programDate(DateConverter.toEpochSecond(date))
						.programCategory(ProgramCategory.WEEKLY)
						.programType(ProgramType.DEMAND)
						.writer(2L)
						.build();

		LocalDate beforeDate = LocalDate.now().minusDays(1L);

		ProgramModel requestModel =
				ProgramModel.builder()
						.title("title 변경")
						.content("content 변경")
						.programDate(DateConverter.toEpochSecond(beforeDate))
						.programCategory(ProgramCategory.EVENT_TEAM)
						.programType(ProgramType.DEMAND)
						.writer(2L)
						.build();

		// when
		ProgramModel update = model.update(requestModel);

		// then
		assertAll(
				() -> assertEquals(update.getProgramCategory(), model.getProgramCategory()),
				() -> assertEquals(update.getProgramType(), model.getProgramType()),
				() -> assertEquals(update.getTitle(), model.getTitle()),
				() -> assertEquals(update.getContent(), model.getContent()),
				() -> assertEquals(update.getProgramDate(), model.getProgramDate()));
	}

	@Test
	@DisplayName("프로그램 생성은 생성 기준 이전 날짜는 불가능하다.")
	void can_create_program_before_date() {
		// given
		LocalDate beforeDate = LocalDate.now().minusDays(1L);

		ProgramModel model =
				ProgramModel.builder()
						.title("title 변경")
						.content("content 변경")
						.programDate(DateConverter.toEpochSecond(beforeDate))
						.programCategory(ProgramCategory.EVENT_TEAM)
						.programType(ProgramType.DEMAND)
						.writer(2L)
						.build();

		// when & then
		assertThrows(OverDateException.class, model::validateCreate);
	}

	@Test
	@DisplayName("프로그램 생성은 생성 기준 이후 날짜는 가능하다.")
	void can_create_program_over_date() {
		// given
		LocalDate beforeDate = LocalDate.now().plusDays(1L);

		ProgramModel model =
				ProgramModel.builder()
						.title("title 변경")
						.content("content 변경")
						.programDate(DateConverter.toEpochSecond(beforeDate))
						.programCategory(ProgramCategory.EVENT_TEAM)
						.programType(ProgramType.DEMAND)
						.writer(2L)
						.build();

		// when & then
		assertDoesNotThrow(model::validateCreate);
	}

	@Test
	@DisplayName("프로그램 생성은 생성 기준 당일 날짜는 가능하다.")
	void can_create_program_date() {
		// given
		LocalDate beforeDate = LocalDate.now();

		ProgramModel model =
				ProgramModel.builder()
						.title("title 변경")
						.content("content 변경")
						.programDate(DateConverter.toEpochSecond(beforeDate))
						.programCategory(ProgramCategory.EVENT_TEAM)
						.programType(ProgramType.DEMAND)
						.writer(2L)
						.build();

		// when & then
		assertDoesNotThrow(model::validateCreate);
	}
}
