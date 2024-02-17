package com.blackcompany.eeos.teamBuilding.application.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.blackcompany.eeos.target.application.service.SelectTeamBuildingTargetService;
import com.blackcompany.eeos.teamBuilding.application.dto.CreateTeamBuildingRequest;
import com.blackcompany.eeos.teamBuilding.application.model.converter.TeamBuildingEntityConverter;
import com.blackcompany.eeos.teamBuilding.application.model.converter.TeamBuildingRequestConverter;
import com.blackcompany.eeos.teamBuilding.fixture.TeamBuildingFixture;
import com.blackcompany.eeos.teamBuilding.persistence.TeamBuildingRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommandTeamBuildingServiceTest {
	@Spy private TeamBuildingRequestConverter requestConverter;
	@Spy private TeamBuildingEntityConverter entityConverter;
	@Mock private TeamBuildingRepository teamBuildingRepository;
	@Mock private SelectTeamBuildingTargetService teamBuildingTargetService;
	@InjectMocks private CommandTeamBuildingService commandTeamBuildingService;

	@Test
	@DisplayName("팀빌딩 생성 시 팀빌딩 대상자를 선정한다.")
	void save_team_building() {
		// given
		CreateTeamBuildingRequest 팀빌딩_생성_요청 = TeamBuildingFixture.팀빌딩_생성_요청();
		when(teamBuildingRepository.save(any())).thenReturn(TeamBuildingFixture.팀빌딩());

		// when
		commandTeamBuildingService.create(1L, 팀빌딩_생성_요청);

		// then
		verify(teamBuildingTargetService)
				.save(TeamBuildingFixture.팀빌딩().getId(), 팀빌딩_생성_요청.getMembers());
	}
}
