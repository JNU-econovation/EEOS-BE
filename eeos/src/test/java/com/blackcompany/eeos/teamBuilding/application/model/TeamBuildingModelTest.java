package com.blackcompany.eeos.teamBuilding.application.model;

import static org.junit.jupiter.api.Assertions.*;

import com.blackcompany.eeos.teamBuilding.application.exception.DeniedEditTeamBuilding;
import com.blackcompany.eeos.teamBuilding.fixture.TeamBuildingFixture;
import com.blackcompany.eeos.teamBuilding.persistence.TeamBuildingStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TeamBuildingModelTest {
	@Test
	@DisplayName("해당 팀빌딩에 수정권한이 있는 유저이면 예외가 발생한다.")
	void cannot_edit() {
		// given
		TeamBuildingModel model = TeamBuildingFixture.팀빌딩_모델(TeamBuildingStatus.PROGRESS, 1L);

		// when & then
		assertThrows(DeniedEditTeamBuilding.class, () -> model.validateEdit(2L));
	}

	@Test
	@DisplayName("해당 팀빌딩에 수정권한이 없는 유저이면 예외가 발생하지 않는다.")
	void can_edit() {
		// given
		TeamBuildingModel model = TeamBuildingFixture.팀빌딩_모델(TeamBuildingStatus.PROGRESS, 1L);

		// when & then
		Assertions.assertDoesNotThrow(() -> model.validateEdit(1L));
	}
}
