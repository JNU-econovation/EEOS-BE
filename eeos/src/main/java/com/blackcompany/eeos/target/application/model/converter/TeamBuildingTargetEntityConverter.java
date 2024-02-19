package com.blackcompany.eeos.target.application.model.converter;

import com.blackcompany.eeos.common.support.converter.AbstractEntityConverter;
import com.blackcompany.eeos.target.application.model.TeamBuildingTargetModel;
import com.blackcompany.eeos.target.persistence.TeamBuildingInputDataEntity;
import com.blackcompany.eeos.target.persistence.TeamBuildingInputStatus;
import com.blackcompany.eeos.target.persistence.TeamBuildingTargetEntity;
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
				.inputStatus(entity.getInputData().getInputStatus().getStatus())
				.build();
	}

	@Override
	public TeamBuildingTargetEntity toEntity(TeamBuildingTargetModel model) {
		return TeamBuildingTargetEntity.builder()
				.id(model.getId())
				.teamBuildingId(model.getTeamBuildingId())
				.memberId(model.getMemberId())
				.inputData(toInputDataEntity(model))
				.build();
	}

	public TeamBuildingTargetEntity toEntity(Long teamBuildingId, Long memberId) {
		return TeamBuildingTargetEntity.builder()
				.teamBuildingId(teamBuildingId)
				.memberId(memberId)
				.build();
	}

	private TeamBuildingInputDataEntity toInputDataEntity(TeamBuildingTargetModel model) {
		return TeamBuildingInputDataEntity.builder()
				.content(model.getContent())
				.inputStatus(TeamBuildingInputStatus.find(model.getInputStatus()))
				.build();
	}
}
