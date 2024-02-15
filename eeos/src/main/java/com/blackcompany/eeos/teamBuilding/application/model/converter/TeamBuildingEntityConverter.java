package com.blackcompany.eeos.teamBuilding.application.model.converter;

import com.blackcompany.eeos.common.support.converter.AbstractEntityConverter;
import com.blackcompany.eeos.teamBuilding.application.model.TeamBuildingModel;
import com.blackcompany.eeos.teamBuilding.persistence.TeamBuildingEntity;
import org.springframework.stereotype.Component;

@Component
public class TeamBuildingEntityConverter
		implements AbstractEntityConverter<TeamBuildingEntity, TeamBuildingModel> {
	@Override
	public TeamBuildingModel from(TeamBuildingEntity entity) {
		return TeamBuildingModel.builder()
				.id(entity.getId())
				.title(entity.getTitle())
				.content(entity.getContent())
				.maxTeamSize(entity.getMaxTeamSize())
				.build();
	}

	@Override
	public TeamBuildingEntity toEntity(TeamBuildingModel model) {
		return TeamBuildingEntity.builder()
				.id(model.getId())
				.title(model.getTitle())
				.content(model.getContent())
				.maxTeamSize(model.getMaxTeamSize())
				.build();
	}
}
