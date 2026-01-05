package com.blackcompany.eeos.team.fixture;

import com.blackcompany.eeos.team.application.dto.CreateTeamRequest;
import com.blackcompany.eeos.team.application.model.TeamModel;
import com.blackcompany.eeos.team.persistence.TeamEntity;

public class TeamFixture {

	public static TeamModel 팀_모델(Long teamId) {
		return TeamModel.builder().id(teamId).name("테스트팀").status(true).build();
	}

	public static TeamModel 팀_모델_이름(Long teamId, String name) {
		return TeamModel.builder().id(teamId).name(name).status(true).build();
	}

	public static TeamModel 비활성_팀_모델(Long teamId) {
		return TeamModel.builder().id(teamId).name("비활성팀").status(false).build();
	}

	public static TeamEntity 팀_엔티티(Long teamId) {
		return TeamEntity.builder().id(teamId).name("테스트팀").status(true).build();
	}

	public static TeamEntity 팀_엔티티_이름(Long teamId, String name) {
		return TeamEntity.builder().id(teamId).name(name).status(true).build();
	}

	public static TeamEntity 비활성_팀_엔티티(Long teamId, String name) {
		return TeamEntity.builder().id(teamId).name(name).status(false).build();
	}

	public static CreateTeamRequest 팀_생성_요청() {
		return CreateTeamRequest.builder().teamName("새로운팀").build();
	}

	public static CreateTeamRequest 팀_생성_요청_이름(String name) {
		return CreateTeamRequest.builder().teamName(name).build();
	}
}
