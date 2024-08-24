package com.blackcompany.eeos.program.application.model;

import static org.junit.jupiter.api.Assertions.*;

import com.blackcompany.eeos.program.application.exception.*;
import com.blackcompany.eeos.program.fixture.ProgramFixture;
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
		ProgramModel model = ProgramFixture.프로그램_모델(LocalDate.now().plusDays(1L));

		// when
		String programStatus = model.getProgramStatus();

		// then
		assertEquals(ProgramStatus.ACTIVE.getStatus(), programStatus);
	}

	@Test
	@DisplayName("프로그램 날짜가 현 날짜와 같으면 상태는 진행중(active)이다.")
	void calculateStatusDay() {
		// given
		ProgramModel model = ProgramFixture.프로그램_모델(LocalDate.now());

		// when
		String programStatus = model.getProgramStatus();

		// then
		assertEquals(ProgramStatus.ACTIVE.getStatus(), programStatus);
	}

	@Test
	@DisplayName("프로그램 날짜가 현 날짜의 이전이라면 상태는 완료(end)이다.")
	void calculateStatusBeforeDay() {
		// given
		ProgramModel model = ProgramFixture.프로그램_모델(LocalDate.now().minusDays(1L));

		// when
		String programStatus = model.getProgramStatus();

		// then
		assertEquals(ProgramStatus.END.getStatus(), programStatus);
	}

	@Test
	@DisplayName("작성자 본인일 경우 프로그램 삭제가 가능하다.")
	void can_delete() {
		// given
		ProgramModel model = ProgramFixture.프로그램_모델(LocalDate.now().minusDays(1L), 1L);

		// when & then
		assertDoesNotThrow(() -> model.validateDelete(1L));
	}

	@Test
	@DisplayName("작성자 본인이 아닐 경우 프로그램 삭제가 불가능하다.")
	void cannot_delete() {
		// given
		ProgramModel model = ProgramFixture.프로그램_모델(LocalDate.now().minusDays(1L), 2L);

		// when & then
		assertThrows(DeniedProgramEditException.class, () -> model.validateDelete(1L));
	}

	@Test
	@DisplayName("완료(end)된 프로그램의 참석 대상자를 작성자는 수정할 수 있다.")
	void can_edit_when_end_program() {
		// given
		ProgramModel model = ProgramFixture.프로그램_모델(LocalDate.now().minusDays(1L), 2L);

		// when & then
		assertDoesNotThrow(() -> model.validateEditAttend(2L));
	}

	@Test
	@DisplayName("진행중(active)인 프로그램의 참석 대상자는 수정할 수 없다")
	void cannot_edit_when_active_program() {
		// given
		ProgramModel model = ProgramFixture.프로그램_모델(LocalDate.now().plusDays(1L), 2L);

		// when & then
		assertThrows(NotAllowedUpdatedProgramAttendException.class, () -> model.validateEditAttend(2L));
	}

	@Test
	@DisplayName("프로그램의 작성자가 아닐 시에 참석 대상자를 수정하지 못 한다.")
	void cannot_edit_when_not_writer() {
		// given
		ProgramModel model = ProgramFixture.프로그램_모델(LocalDate.now().plusDays(1L), 2L);

		// when & then
		assertThrows(DeniedProgramEditException.class, () -> model.validateEditAttend(1L));
	}

	@Test
	@DisplayName("프로그램 타입은 수정하지 못 한다.")
	void cannot_edit_program_type() {
		// given
		ProgramModel model =
				ProgramFixture.프로그램_모델(LocalDate.now().plusDays(1L), ProgramType.NOTIFICATION, 2L);
		ProgramModel requestModel =
				ProgramFixture.프로그램_모델(LocalDate.now().plusDays(1L), ProgramType.DEMAND, 2L);

		// when & then
		assertThrows(NotAllowedUpdatedProgramTypeException.class, () -> model.update(requestModel));
	}

	@Test
	@DisplayName("all로 프로그램 카테고리를 수정하지 못 한다.")
	void cannot_edit_program_category_all() {
		// given
		ProgramModel model =
				ProgramFixture.프로그램_모델(
						LocalDate.now().plusDays(1L), ProgramCategory.EVENT_TEAM, ProgramType.DEMAND, 1L);
		ProgramModel requestModel =
				ProgramFixture.프로그램_모델(
						LocalDate.now().plusDays(1L), ProgramCategory.ALL, ProgramType.DEMAND, 1L);

		// when & then
		assertThrows(NotFoundProgramCategoryException.class, () -> model.update(requestModel));
	}

	@Test
	@DisplayName("프로그램 작성자가 아니면 프로그램은 수정하지 못 한다.")
	void cannot_edit_program_when_not_writer() {
		// given
		ProgramModel model = ProgramFixture.프로그램_모델(LocalDate.now().plusDays(1L), 2L);
		ProgramModel requestModel = ProgramFixture.프로그램_모델(LocalDate.now().plusDays(1L), 1L);

		// when & then
		assertThrows(DeniedProgramEditException.class, () -> model.update(requestModel));
	}

	@Test
	@DisplayName("프로그램 작성자이며 프로그램 타입을 수정하지 않을 때는 프로그램 수정이 가능하다.")
	void can_edit_program() {
		// given
		ProgramModel model =
				ProgramFixture.프로그램_모델(
						LocalDate.now().plusDays(1L), ProgramCategory.WEEKLY, ProgramType.DEMAND, 2L);
		ProgramModel requestModel =
				ProgramFixture.프로그램_모델(
						LocalDate.now().plusDays(1L), ProgramCategory.EVENT_TEAM, ProgramType.DEMAND, 2L);

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
		ProgramModel model =
				ProgramFixture.프로그램_모델(
						LocalDate.now().plusDays(1L), ProgramCategory.WEEKLY, ProgramType.DEMAND, 2L);
		ProgramModel requestModel =
				ProgramFixture.프로그램_모델(
						LocalDate.now().minusDays(1L), ProgramCategory.EVENT_TEAM, ProgramType.DEMAND, 2L);

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
	void cannot_create_program_before_date() {
		// given
		ProgramModel model =
				ProgramFixture.프로그램_모델(
						LocalDate.now().minusDays(1L), ProgramCategory.EVENT_TEAM, ProgramType.DEMAND, 2L);

		// when & then
		assertThrows(OverDateException.class, model::validateCreate);
	}

	@Test
	@DisplayName("프로그램 생성은 생성 기준 이후 날짜는 가능하다.")
	void can_create_program_over_date() {
		// given
		ProgramModel model =
				ProgramFixture.프로그램_모델(
						LocalDate.now().plusDays(1L), ProgramCategory.EVENT_TEAM, ProgramType.DEMAND, 2L);

		// when & then
		assertDoesNotThrow(model::validateCreate);
	}

	@Test
	@DisplayName("프로그램 생성은 생성 기준 당일 날짜는 가능하다.")
	void can_create_program_date() {
		// given
		ProgramModel model =
				ProgramFixture.프로그램_모델(LocalDate.now(), ProgramCategory.ETC, ProgramType.DEMAND, 1L);

		// when & then
		assertDoesNotThrow(model::validateCreate);
	}
}
