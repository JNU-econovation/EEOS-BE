package com.blackcompany.eeos.team.application.dto.converter;

import com.blackcompany.eeos.team.application.dto.CreateTeamRequest;
import com.blackcompany.eeos.team.application.model.TeamModel;
import org.springframework.stereotype.Component;

@Component
public class CreateTeamRequestConverter {
	public TeamModel from(CreateTeamRequest source) {
		return TeamModel.builder().name(source.getTeamName()).status(true).build();
	}
}
