package com.blackcompany.eeos.teamBuilding.application.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.blackcompany.eeos.target.application.service.SelectTeamBuildingTargetService;
import com.blackcompany.eeos.teamBuilding.application.dto.CreateTeamBuildingRequest;
import com.blackcompany.eeos.teamBuilding.application.exception.DeniedEditTeamBuilding;
import com.blackcompany.eeos.teamBuilding.application.model.converter.TeamBuildingEntityConverter;
import com.blackcompany.eeos.teamBuilding.application.model.converter.TeamBuildingRequestConverter;
import com.blackcompany.eeos.teamBuilding.fixture.TeamBuildingFixture;
import com.blackcompany.eeos.teamBuilding.persistence.TeamBuildingEntity;
import com.blackcompany.eeos.teamBuilding.persistence.TeamBuildingRepository;
import com.blackcompany.eeos.teamBuilding.persistence.TeamBuildingStatus;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TeamBuildingServiceTest {
	@Spy private TeamBuildingRequestConverter requestConverter;
	@Spy private TeamBuildingEntityConverter entityConverter;
	@Mock private TeamBuildingRepository teamBuildingRepository;
	@Mock private SelectTeamBuildingTargetService teamBuildingTargetService;
	@Mock private RestrictTeamBuildingService restrictTeamBuildingService;
	@InjectMocks private TeamBuildingService teamBuildingService;

	@Test
	@DisplayName("팀빌딩 생성 시 팀빌딩 대상자를 선정한다.")
	void save_team_building() {
		// given
		CreateTeamBuildingRequest 팀빌딩_생성_요청 = TeamBuildingFixture.팀빌딩_생성_요청(List.of(1L, 2L));
		TeamBuildingEntity 생성한_팀빌딩 = TeamBuildingFixture.팀빌딩(TeamBuildingStatus.PROGRESS, 1L);
		when(teamBuildingRepository.save(any())).thenReturn(생성한_팀빌딩);

		// when
		teamBuildingService.create(1L, 팀빌딩_생성_요청);

		// then
		verify(teamBuildingTargetService).save(생성한_팀빌딩.getId(), 팀빌딩_생성_요청.getMembers());
	}

	@Test
	@DisplayName("진행 중인 팀빌딩의 작성자가 아니라면 수정 권한이 없음 예외가 발생한다.")
	void denied_edit_team_building() {
		// given
		TeamBuildingEntity 생성된_팀빌딩 = TeamBuildingFixture.팀빌딩(TeamBuildingStatus.COMPLETE, 1L);
		when(teamBuildingRepository.findByStatus(TeamBuildingStatus.COMPLETE))
				.thenReturn(Optional.of(생성된_팀빌딩));

		// when & then
		Assertions.assertThrows(DeniedEditTeamBuilding.class, () -> teamBuildingService.delete(2L));
	}
}
