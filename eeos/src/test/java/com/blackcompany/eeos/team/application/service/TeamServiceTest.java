package com.blackcompany.eeos.team.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.blackcompany.eeos.member.application.model.MemberModel;
import com.blackcompany.eeos.member.application.service.QueryMemberService;
import com.blackcompany.eeos.target.persistence.presentation.PresentationRepository;
import com.blackcompany.eeos.team.application.Service.TeamService;
import com.blackcompany.eeos.team.application.dto.CreateTeamRequest;
import com.blackcompany.eeos.team.application.dto.CreateTeamResponse;
import com.blackcompany.eeos.team.application.dto.QueryTeamsResponse;
import com.blackcompany.eeos.team.application.dto.converter.CreateTeamRequestConverter;
import com.blackcompany.eeos.team.application.dto.converter.QueryTeamResponseConverter;
import com.blackcompany.eeos.team.application.dto.converter.TeamResponseConverter;
import com.blackcompany.eeos.team.application.exception.DeniedTeamEditException;
import com.blackcompany.eeos.team.application.exception.DuplicateTeamNameException;
import com.blackcompany.eeos.team.application.exception.NotFoundTeamException;
import com.blackcompany.eeos.team.application.exception.NotFoundTeamStatusException;
import com.blackcompany.eeos.team.application.model.TeamModel;
import com.blackcompany.eeos.team.application.model.converter.TeamEntityConverter;
import com.blackcompany.eeos.team.fixture.TeamFixture;
import com.blackcompany.eeos.team.persistence.TeamEntity;
import com.blackcompany.eeos.team.persistence.TeamRepository;
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
class TeamServiceTest {

	@Mock CreateTeamRequestConverter createTeamRequestConverter;
	@Mock TeamEntityConverter entityConverter;
	@Mock TeamRepository teamRepository;
	@Mock TeamResponseConverter teamResponseConverter;
	@Mock ApplicationEventPublisher applicationEventPublisher;
	@Mock QueryTeamResponseConverter queryTeamResponseConverter;
	@Mock QueryMemberService memberService;
	@Mock PresentationRepository presentationRepository;
	@InjectMocks TeamService teamService;

	private static final Long ADMIN_MEMBER_ID = 1L;
	private static final Long NON_ADMIN_MEMBER_ID = 2L;
	private static final Long TEAM_ID = 1L;

	@Test
	@DisplayName("어드민이 팀을 생성할 수 있다")
	void create_team_success() {
		// given
		CreateTeamRequest request = TeamFixture.팀_생성_요청();
		TeamModel model = TeamFixture.팀_모델_이름(null, "새로운팀");
		TeamEntity entity = TeamFixture.팀_엔티티_이름(TEAM_ID, "새로운팀");
		MemberModel adminMember = MemberModel.builder().id(ADMIN_MEMBER_ID).isAdmin(true).build();
		CreateTeamResponse response = new CreateTeamResponse(TEAM_ID);

		when(memberService.findMember(ADMIN_MEMBER_ID)).thenReturn(adminMember);
		when(createTeamRequestConverter.from(request)).thenReturn(model);
		when(teamRepository.findTeamEntityByName("새로운팀")).thenReturn(List.of());
		when(entityConverter.toEntity(model)).thenReturn(entity);
		when(teamRepository.save(any(TeamEntity.class))).thenReturn(entity);
		when(teamResponseConverter.from(TEAM_ID)).thenReturn(response);

		// when
		CreateTeamResponse result = teamService.create(ADMIN_MEMBER_ID, request);

		// then
		assertNotNull(result);
		assertEquals(TEAM_ID, result.getTeamId());
		verify(teamRepository).save(any(TeamEntity.class));
	}

	@Test
	@DisplayName("어드민이 아니면 팀을 생성할 수 없다")
	void create_team_denied_not_admin() {
		// given
		CreateTeamRequest request = TeamFixture.팀_생성_요청();
		MemberModel nonAdminMember =
				MemberModel.builder().id(NON_ADMIN_MEMBER_ID).isAdmin(false).build();

		when(memberService.findMember(NON_ADMIN_MEMBER_ID)).thenReturn(nonAdminMember);

		// when & then
		assertThrows(
				DeniedTeamEditException.class, () -> teamService.create(NON_ADMIN_MEMBER_ID, request));
	}

	@Test
	@DisplayName("중복된 팀 이름이면 생성할 수 없다")
	void create_team_duplicate_name() {
		// given
		CreateTeamRequest request = TeamFixture.팀_생성_요청_이름("중복팀");
		TeamModel model = TeamFixture.팀_모델_이름(null, "중복팀");
		TeamEntity existingEntity = TeamFixture.팀_엔티티_이름(TEAM_ID, "중복팀");
		MemberModel adminMember = MemberModel.builder().id(ADMIN_MEMBER_ID).isAdmin(true).build();

		when(memberService.findMember(ADMIN_MEMBER_ID)).thenReturn(adminMember);
		when(createTeamRequestConverter.from(request)).thenReturn(model);
		when(teamRepository.findTeamEntityByName("중복팀")).thenReturn(List.of(existingEntity));

		// when & then
		assertThrows(
				DuplicateTeamNameException.class, () -> teamService.create(ADMIN_MEMBER_ID, request));
	}

	@Test
	@DisplayName("비활성 상태인 동일 이름 팀이 있으면 활성화한다")
	void create_team_reactivate_inactive_team() {
		// given
		CreateTeamRequest request = TeamFixture.팀_생성_요청_이름("비활성팀");
		TeamModel model = TeamFixture.팀_모델_이름(null, "비활성팀");
		TeamEntity inactiveEntity = TeamFixture.비활성_팀_엔티티(TEAM_ID, "비활성팀");
		MemberModel adminMember = MemberModel.builder().id(ADMIN_MEMBER_ID).isAdmin(true).build();
		CreateTeamResponse response = new CreateTeamResponse(TEAM_ID);

		when(memberService.findMember(ADMIN_MEMBER_ID)).thenReturn(adminMember);
		when(createTeamRequestConverter.from(request)).thenReturn(model);
		when(teamRepository.findTeamEntityByName("비활성팀")).thenReturn(List.of(inactiveEntity));
		when(teamResponseConverter.from(TEAM_ID)).thenReturn(response);

		// when
		CreateTeamResponse result = teamService.create(ADMIN_MEMBER_ID, request);

		// then
		assertNotNull(result);
		assertEquals(TEAM_ID, result.getTeamId());
		assertTrue(inactiveEntity.isStatus());
	}

	@Test
	@DisplayName("어드민이 팀을 삭제할 수 있다")
	void delete_team_success() {
		// given
		TeamEntity entity = TeamFixture.팀_엔티티(TEAM_ID);
		TeamModel model = TeamFixture.팀_모델(TEAM_ID);
		MemberModel adminMember = MemberModel.builder().id(ADMIN_MEMBER_ID).isAdmin(true).build();

		when(teamRepository.findById(TEAM_ID)).thenReturn(Optional.of(entity));
		when(entityConverter.from(entity)).thenReturn(model);
		when(memberService.findMember(ADMIN_MEMBER_ID)).thenReturn(adminMember);
		doNothing().when(teamRepository).deleteTeamEntityByName(TEAM_ID);
		doNothing().when(applicationEventPublisher).publishEvent(any());

		// when
		teamService.delete(ADMIN_MEMBER_ID, TEAM_ID);

		// then
		verify(teamRepository).deleteTeamEntityByName(TEAM_ID);
		verify(applicationEventPublisher).publishEvent(any());
	}

	@Test
	@DisplayName("어드민이 아니면 팀을 삭제할 수 없다")
	void delete_team_denied_not_admin() {
		// given
		TeamEntity entity = TeamFixture.팀_엔티티(TEAM_ID);
		TeamModel model = TeamFixture.팀_모델(TEAM_ID);
		MemberModel nonAdminMember =
				MemberModel.builder().id(NON_ADMIN_MEMBER_ID).isAdmin(false).build();

		when(teamRepository.findById(TEAM_ID)).thenReturn(Optional.of(entity));
		when(entityConverter.from(entity)).thenReturn(model);
		when(memberService.findMember(NON_ADMIN_MEMBER_ID)).thenReturn(nonAdminMember);

		// when & then
		assertThrows(
				DeniedTeamEditException.class, () -> teamService.delete(NON_ADMIN_MEMBER_ID, TEAM_ID));
	}

	@Test
	@DisplayName("존재하지 않는 팀을 삭제하면 예외가 발생한다")
	void delete_team_not_found() {
		// given
		when(teamRepository.findById(TEAM_ID)).thenReturn(Optional.empty());

		// when & then
		assertThrows(NotFoundTeamException.class, () -> teamService.delete(ADMIN_MEMBER_ID, TEAM_ID));
	}

	@Test
	@DisplayName("none 파라미터로 모든 활성 팀을 조회할 수 있다")
	void get_teams_by_none() {
		// given
		TeamEntity entity1 = TeamFixture.팀_엔티티_이름(1L, "팀1");
		TeamEntity entity2 = TeamFixture.팀_엔티티_이름(2L, "팀2");
		TeamModel model1 = TeamFixture.팀_모델_이름(1L, "팀1");
		TeamModel model2 = TeamFixture.팀_모델_이름(2L, "팀2");
		QueryTeamsResponse response = new QueryTeamsResponse(List.of());

		when(teamRepository.findAllActiveTeams()).thenReturn(List.of(entity1, entity2));
		when(entityConverter.from(entity1)).thenReturn(model1);
		when(entityConverter.from(entity2)).thenReturn(model2);
		when(queryTeamResponseConverter.from(any())).thenReturn(response);

		// when
		QueryTeamsResponse result = teamService.execute("none");

		// then
		assertNotNull(result);
		verify(teamRepository).findAllActiveTeams();
	}

	@Test
	@DisplayName("프로그램 ID로 팀을 조회할 수 있다")
	void get_teams_by_program_id() {
		// given
		Long programId = 1L;
		TeamEntity entity = TeamFixture.팀_엔티티(TEAM_ID);
		TeamModel model = TeamFixture.팀_모델(TEAM_ID);
		QueryTeamsResponse response = new QueryTeamsResponse(List.of());

		when(presentationRepository.findTeamsByProgramId(programId)).thenReturn(List.of(entity));
		when(entityConverter.from(entity)).thenReturn(model);
		when(queryTeamResponseConverter.from(any())).thenReturn(response);

		// when
		QueryTeamsResponse result = teamService.execute("1");

		// then
		assertNotNull(result);
		verify(presentationRepository).findTeamsByProgramId(programId);
	}

	@Test
	@DisplayName("잘못된 프로그램 ID 형식이면 예외가 발생한다")
	void get_teams_invalid_program_id_format() {
		// when & then
		assertThrows(NotFoundTeamStatusException.class, () -> teamService.execute("invalid"));
	}
}
