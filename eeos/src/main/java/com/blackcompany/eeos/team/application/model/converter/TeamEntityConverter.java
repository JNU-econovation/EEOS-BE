package com.blackcompany.eeos.team.application.model.converter;

import com.blackcompany.eeos.common.support.converter.AbstractEntityConverter;
import com.blackcompany.eeos.team.application.model.TeamModel;
import com.blackcompany.eeos.team.persistence.TeamEntity;
import org.springframework.stereotype.Component;

@Component
public class TeamEntityConverter implements AbstractEntityConverter<TeamEntity, TeamModel> {

	@Override
	public TeamEntity toEntity(TeamModel source) {
		return TeamEntity.builder().name(source.getName()).status(source.isStatus()).build();
	}

	@Override
	public TeamModel from(TeamEntity source) {
		return TeamModel.builder()
				.id(source.getId())
				.name(source.getName())
				.status((source.isStatus()))
				.build();
	}
}
