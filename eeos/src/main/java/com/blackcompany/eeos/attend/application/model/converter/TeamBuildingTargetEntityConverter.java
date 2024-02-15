package com.blackcompany.eeos.attend.application.model.converter;

import com.blackcompany.eeos.attend.application.model.TeamBuildingTargetModel;
import com.blackcompany.eeos.attend.persistence.TeamBuildingInputDataEntity;
import com.blackcompany.eeos.attend.persistence.TeamBuildingTargetEntity;
import com.blackcompany.eeos.common.support.converter.AbstractEntityConverter;
import org.springframework.stereotype.Component;

@Component
public class TeamBuildingTargetEntityConverter
		implements AbstractEntityConverter<TeamBuildingTargetEntity, TeamBuildingTargetModel> {
	@Override
	public TeamBuildingTargetModel from(TeamBuildingTargetEntity entity) {
		return TeamBuildingTargetModel.builder()
				.id(entity.getId())
				.teamBuildingId(entity.getTeamBuildingId())
				.memberId(entity.getMemberId())
				.content(entity.getInputData().getContent())
				.build();
	}

	@Override
	public TeamBuildingTargetEntity toEntity(TeamBuildingTargetModel model) {
		return TeamBuildingTargetEntity.builder()
				.id(model.getId())
				.teamBuildingId(model.getTeamBuildingId())
				.memberId(model.getMemberId())
				.inputData(toEntity(model.getContent()))
				.build();
	}

	public TeamBuildingTargetEntity toEntity(Long teamBuildingId, Long memberId) {
		return TeamBuildingTargetEntity.builder()
				.teamBuildingId(teamBuildingId)
				.memberId(memberId)
				.build();
	}

	private TeamBuildingInputDataEntity toEntity(String content) {
		return TeamBuildingInputDataEntity.builder().content(content).build();
	}
}
