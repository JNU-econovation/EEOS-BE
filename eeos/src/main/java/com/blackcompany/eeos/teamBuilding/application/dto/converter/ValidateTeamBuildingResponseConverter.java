package com.blackcompany.eeos.teamBuilding.application.dto.converter;

import com.blackcompany.eeos.teamBuilding.application.dto.ValidateTeamBuildingResponse;
import org.springframework.stereotype.Component;

@Component
public class ValidateTeamBuildingResponseConverter {
	public ValidateTeamBuildingResponse from(String status) {
		return ValidateTeamBuildingResponse.builder().status(status).build();
	}
}
