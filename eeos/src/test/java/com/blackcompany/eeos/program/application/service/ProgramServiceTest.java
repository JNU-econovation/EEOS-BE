package com.blackcompany.eeos.program.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.blackcompany.eeos.member.application.model.MemberModel;
import com.blackcompany.eeos.member.application.service.QueryMemberService;
import com.blackcompany.eeos.program.application.dto.CommandProgramResponse;
import com.blackcompany.eeos.program.application.dto.CreateProgramRequest;
import com.blackcompany.eeos.program.application.dto.QueryProgramResponse;
import com.blackcompany.eeos.program.application.dto.converter.ProgramPageResponseConverter;
import com.blackcompany.eeos.program.application.dto.converter.ProgramResponseConverter;
import com.blackcompany.eeos.program.application.dto.converter.QueryAccessRightResponseConverter;
import com.blackcompany.eeos.program.application.exception.DeniedProgramEditException;
import com.blackcompany.eeos.program.application.exception.NotFoundProgramException;
import com.blackcompany.eeos.program.application.model.AccessRights;
import com.blackcompany.eeos.program.application.model.ProgramModel;
import com.blackcompany.eeos.program.application.model.converter.ProgramEntityConverter;
import com.blackcompany.eeos.program.application.model.converter.ProgramRequestConverter;
import com.blackcompany.eeos.program.application.support.ProgramStatusServiceComposite;
import com.blackcompany.eeos.program.application.usecase.ProgramQuitUsecase;
import com.blackcompany.eeos.program.fixture.ProgramFixture;
import com.blackcompany.eeos.program.infra.api.slack.chat.service.ProgramNotifyServiceComposite;
import com.blackcompany.eeos.program.persistence.ProgramCategory;
import com.blackcompany.eeos.program.persistence.ProgramEntity;
import com.blackcompany.eeos.program.persistence.ProgramRepository;
import com.blackcompany.eeos.program.persistence.ProgramType;
import com.blackcompany.eeos.target.application.service.SelectAttendCommandTargetMemberMemberService;
import com.blackcompany.eeos.target.application.usecase.PresentTeamUsecase;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class ProgramServiceTest {

	@Mock ProgramRequestConverter requestConverter;
	@Mock ProgramEntityConverter entityConverter;
	@Mock ProgramResponseConverter responseConverter;
	@Mock ProgramRepository programRepository;
	@Mock SelectAttendCommandTargetMemberMemberService attendTargetService;
	@Mock ProgramPageResponseConverter pageResponseConverter;
	@Mock ProgramStatusServiceComposite programStatusComposite;
	@Mock ApplicationEventPublisher applicationEventPublisher;
	@Mock QueryAccessRightResponseConverter accessRightResponseConverter;
	@Mock ProgramNotifyServiceComposite notifyServiceComposite;
	@Mock QueryMemberService memberService;
	@Mock ProgramQuitUsecase quitUsecase;
	@Mock PresentTeamUsecase presentTeamUsecase;
	@InjectMocks ProgramService programService;

	private static final Long ADMIN_MEMBER_ID = 1L;
	private static final Long NON_ADMIN_MEMBER_ID = 2L;
	private static final Long PROGRAM_ID = 1L;
	private static final String GITHUB_URL = "https://github.com/JNU-econovation/test";

	@Test
	@DisplayName("어드민이 프로그램을 생성할 수 있다")
	void create_program_success() {
		// given
		LocalDate futureDate = LocalDate.now().plusDays(7);
		CreateProgramRequest request =
				CreateProgramRequest.builder()
						.title("테스트 프로그램")
						.deadLine(Timestamp.valueOf(futureDate.atStartOfDay()))
						.content("테스트 내용")
						.category("weekly")
						.type("demand")
						.teams(List.of())
						.programGithubUrl(GITHUB_URL)
						.members(List.of())
						.build();

		ProgramModel model = ProgramFixture.프로그램_모델(futureDate, ADMIN_MEMBER_ID);
		ProgramEntity entity =
				ProgramEntity.builder()
						.id(PROGRAM_ID)
						.title("테스트 프로그램")
						.programCategory(ProgramCategory.WEEKLY)
						.programType(ProgramType.DEMAND)
						.writer(ADMIN_MEMBER_ID)
						.build();
		MemberModel adminMember = MemberModel.builder().id(ADMIN_MEMBER_ID).isAdmin(true).build();
		CommandProgramResponse response =
				CommandProgramResponse.builder().programId(PROGRAM_ID).build();

		when(memberService.findMember(ADMIN_MEMBER_ID)).thenReturn(adminMember);
		when(requestConverter.from(ADMIN_MEMBER_ID, request)).thenReturn(model);
		when(entityConverter.toEntity(any(ProgramModel.class))).thenReturn(entity);
		when(programRepository.save(any(ProgramEntity.class))).thenReturn(entity);
		doNothing().when(attendTargetService).save(any(), any());
		doNothing().when(presentTeamUsecase).save(any(), any());
		doNothing().when(quitUsecase).reserveQuitProgram(any());
		doNothing().when(applicationEventPublisher).publishEvent(any());
		when(responseConverter.from(PROGRAM_ID)).thenReturn(response);

		// when
		CommandProgramResponse result = programService.create(ADMIN_MEMBER_ID, request);

		// then
		assertNotNull(result);
		assertEquals(PROGRAM_ID, result.getProgramId());
		verify(programRepository).save(any(ProgramEntity.class));
	}

	@Test
	@DisplayName("어드민이 아니면 프로그램을 생성할 수 없다")
	void create_program_denied_not_admin() {
		// given
		CreateProgramRequest request =
				CreateProgramRequest.builder()
						.title("테스트 프로그램")
						.deadLine(Timestamp.valueOf(LocalDate.now().plusDays(7).atStartOfDay()))
						.content("테스트 내용")
						.category("weekly")
						.type("demand")
						.teams(List.of())
						.programGithubUrl(GITHUB_URL)
						.members(List.of())
						.build();
		MemberModel nonAdminMember =
				MemberModel.builder().id(NON_ADMIN_MEMBER_ID).isAdmin(false).build();

		when(memberService.findMember(NON_ADMIN_MEMBER_ID)).thenReturn(nonAdminMember);

		// when & then
		assertThrows(
				DeniedProgramEditException.class,
				() -> programService.create(NON_ADMIN_MEMBER_ID, request));
	}

	@Test
	@DisplayName("프로그램을 조회할 수 있다")
	void get_program_success() {
		// given
		LocalDate futureDate = LocalDate.now().plusDays(7);
		ProgramModel model =
				ProgramFixture.프로그램_모델(futureDate, ADMIN_MEMBER_ID).toBuilder().id(PROGRAM_ID).build();
		ProgramEntity entity =
				ProgramEntity.builder()
						.id(PROGRAM_ID)
						.title("테스트 프로그램")
						.programCategory(ProgramCategory.WEEKLY)
						.programType(ProgramType.DEMAND)
						.writer(ADMIN_MEMBER_ID)
						.build();
		QueryProgramResponse response =
				QueryProgramResponse.builder()
						.programId(PROGRAM_ID)
						.title("테스트 프로그램")
						.accessRight(AccessRights.EDIT.getAccessRight())
						.build();

		when(programRepository.findById(PROGRAM_ID)).thenReturn(Optional.of(entity));
		when(entityConverter.from(entity)).thenReturn(model);
		when(responseConverter.from(
						any(ProgramModel.class), anyString(), eq(AccessRights.EDIT.getAccessRight())))
				.thenReturn(response);

		// when
		QueryProgramResponse result = programService.getProgram(ADMIN_MEMBER_ID, PROGRAM_ID);

		// then
		assertNotNull(result);
		assertEquals(PROGRAM_ID, result.getProgramId());
	}

	@Test
	@DisplayName("존재하지 않는 프로그램 조회시 예외가 발생한다")
	void get_program_not_found() {
		// given
		when(programRepository.findById(PROGRAM_ID)).thenReturn(Optional.empty());

		// when & then
		assertThrows(
				NotFoundProgramException.class,
				() -> programService.getProgram(ADMIN_MEMBER_ID, PROGRAM_ID));
	}

	@Test
	@DisplayName("작성자가 프로그램을 삭제할 수 있다")
	void delete_program_success() {
		// given
		LocalDate futureDate = LocalDate.now().plusDays(7);
		ProgramModel model =
				ProgramFixture.프로그램_모델(futureDate, ADMIN_MEMBER_ID).toBuilder().id(PROGRAM_ID).build();
		ProgramEntity entity =
				ProgramEntity.builder()
						.id(PROGRAM_ID)
						.title("테스트 프로그램")
						.programCategory(ProgramCategory.WEEKLY)
						.programType(ProgramType.DEMAND)
						.writer(ADMIN_MEMBER_ID)
						.build();

		when(programRepository.findById(PROGRAM_ID)).thenReturn(Optional.of(entity));
		when(entityConverter.from(entity)).thenReturn(model);
		doNothing().when(programRepository).deleteById(PROGRAM_ID);
		doNothing().when(applicationEventPublisher).publishEvent(any());

		// when
		programService.delete(ADMIN_MEMBER_ID, PROGRAM_ID);

		// then
		verify(programRepository).deleteById(PROGRAM_ID);
		verify(applicationEventPublisher).publishEvent(any());
	}

	@Test
	@DisplayName("작성자가 아니면 프로그램을 삭제할 수 없다")
	void delete_program_denied_not_writer() {
		// given
		LocalDate futureDate = LocalDate.now().plusDays(7);
		ProgramModel model =
				ProgramFixture.프로그램_모델(futureDate, ADMIN_MEMBER_ID).toBuilder().id(PROGRAM_ID).build();
		ProgramEntity entity =
				ProgramEntity.builder()
						.id(PROGRAM_ID)
						.title("테스트 프로그램")
						.programCategory(ProgramCategory.WEEKLY)
						.programType(ProgramType.DEMAND)
						.writer(ADMIN_MEMBER_ID)
						.build();

		when(programRepository.findById(PROGRAM_ID)).thenReturn(Optional.of(entity));
		when(entityConverter.from(entity)).thenReturn(model);

		// when & then
		assertThrows(
				DeniedProgramEditException.class,
				() -> programService.delete(NON_ADMIN_MEMBER_ID, PROGRAM_ID));
	}

	@Test
	@DisplayName("게스트도 프로그램을 조회할 수 있다")
	void get_program_guest() {
		// given
		LocalDate futureDate = LocalDate.now().plusDays(7);
		ProgramModel model =
				ProgramFixture.프로그램_모델(futureDate, ADMIN_MEMBER_ID).toBuilder().id(PROGRAM_ID).build();
		ProgramEntity entity =
				ProgramEntity.builder()
						.id(PROGRAM_ID)
						.title("테스트 프로그램")
						.programCategory(ProgramCategory.WEEKLY)
						.programType(ProgramType.DEMAND)
						.writer(ADMIN_MEMBER_ID)
						.build();
		QueryProgramResponse response =
				QueryProgramResponse.builder()
						.programId(PROGRAM_ID)
						.title("테스트 프로그램")
						.accessRight("guest")
						.build();

		when(programRepository.findById(PROGRAM_ID)).thenReturn(Optional.of(entity));
		when(entityConverter.from(entity)).thenReturn(model);
		when(responseConverter.from(any(ProgramModel.class), anyString(), eq("guest")))
				.thenReturn(response);

		// when
		QueryProgramResponse result = programService.getProgram(PROGRAM_ID);

		// then
		assertNotNull(result);
		assertEquals(PROGRAM_ID, result.getProgramId());
	}
}
