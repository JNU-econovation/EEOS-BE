package com.blackcompany.eeos.team.application.dto.converter;

import com.blackcompany.eeos.team.application.dto.UpdateTeamStatusRequest;
import com.blackcompany.eeos.team.application.model.TeamModel;
import org.springframework.stereotype.Component;

@Component
public class UpdateTeamStatusRequestConverter {
	public TeamModel from(UpdateTeamStatusRequest source, TeamModel existingTeam) {
		return existingTeam.toBuilder().status(false).build();
	}
}
