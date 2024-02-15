package com.blackcompany.eeos.teamBuilding.fixture;

import com.blackcompany.eeos.attend.fixture.TargetMemberFixture;
import com.blackcompany.eeos.teamBuilding.application.dto.CreateTeamBuildingRequest;
import com.blackcompany.eeos.teamBuilding.persistence.TeamBuildingEntity;

public class TeamBuildingFixture {
	public static CreateTeamBuildingRequest 팀빌딩_생성_요청() {
		return CreateTeamBuildingRequest.builder()
				.title("팀빌딩 제목")
				.content("팀빌딩 내용")
				.maxTeamSize(3)
				.members(TargetMemberFixture.수민_바다_팀빌딩_대상자())
				.build();
	}

	public static TeamBuildingEntity 팀빌딩() {
		return TeamBuildingEntity.builder()
				.id(1L)
				.title("팀빌딩 제목")
				.content("팀빌딩 내용")
				.maxTeamSize(3)
				.build();
	}
}
