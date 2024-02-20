package com.blackcompany.eeos.teamBuilding.application.model.converter;

import com.blackcompany.eeos.common.support.converter.AbstractEntityConverter;
import com.blackcompany.eeos.teamBuilding.application.model.TeamBuildingModel;
import com.blackcompany.eeos.teamBuilding.persistence.TeamBuildingEntity;
import com.blackcompany.eeos.teamBuilding.persistence.TeamBuildingStatus;
import org.springframework.stereotype.Component;

@Component
public class TeamBuildingEntityConverter
		implements AbstractEntityConverter<TeamBuildingEntity, TeamBuildingModel> {
	@Override
	public TeamBuildingModel from(TeamBuildingEntity source) {
		return TeamBuildingModel.builder()
				.id(source.getId())
				.title(source.getTitle())
				.content(source.getContent())
				.maxTeamSize(source.getMaxTeamSize())
				.status(source.getStatus().getStatus())
				.memberId(source.getMemberId())
				.build();
	}

	@Override
	public TeamBuildingEntity toEntity(TeamBuildingModel source) {
		return TeamBuildingEntity.builder()
				.id(source.getId())
				.title(source.getTitle())
				.content(source.getContent())
				.maxTeamSize(source.getMaxTeamSize())
				.status(TeamBuildingStatus.find(source.getStatus()))
				.memberId(source.getMemberId())
				.build();
	}

	public TeamBuildingEntity toEntity(TeamBuildingModel source, TeamBuildingStatus status) {
		return TeamBuildingEntity.builder()
				.id(source.getId())
				.title(source.getTitle())
				.content(source.getContent())
				.maxTeamSize(source.getMaxTeamSize())
				.status(status)
				.memberId(source.getMemberId())
				.build();
	}
}
