package com.blackcompany.eeos.team.application.dto.converter;

import com.blackcompany.eeos.team.application.dto.CreateTeamResponse;
import org.springframework.stereotype.Component;

@Component
public class TeamResponseConverter {
	public CreateTeamResponse from(Long id) {

		return CreateTeamResponse.builder().teamId(id).build();
	}
}
