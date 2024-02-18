package com.blackcompany.eeos.teamBuilding.fixture;

import com.blackcompany.eeos.target.fixture.TargetMemberFixture;
import com.blackcompany.eeos.teamBuilding.application.dto.CreateTeamBuildingRequest;
import com.blackcompany.eeos.teamBuilding.application.model.TeamBuildingModel;
import com.blackcompany.eeos.teamBuilding.persistence.TeamBuildingEntity;
import com.blackcompany.eeos.teamBuilding.persistence.TeamBuildingStatus;
import java.util.List;
import java.util.stream.Collectors;

public class TeamBuildingFixture {
	public static CreateTeamBuildingRequest 팀빌딩_생성_요청(List<Long> targetIds) {
		return CreateTeamBuildingRequest.builder()
				.title("title")
				.content("content")
				.maxTeamSize(3)
				.members(targetIds.stream().map(TargetMemberFixture::팀빌딩_대상자).collect(Collectors.toList()))
				.build();
	}

	public static TeamBuildingEntity 팀빌딩(TeamBuildingStatus status, Long writerId) {
		return TeamBuildingEntity.builder()
				.id(1L)
				.title("title")
				.content("content")
				.maxTeamSize(3)
				.status(status)
				.memberId(writerId)
				.build();
	}

	public static TeamBuildingModel 팀빌딩_모델(TeamBuildingStatus status, Long writerId) {
		return TeamBuildingModel.builder()
				.id(1L)
				.title("title")
				.content("content")
				.maxTeamSize(3)
				.status(status.getStatus())
				.memberId(writerId)
				.build();
	}
}
