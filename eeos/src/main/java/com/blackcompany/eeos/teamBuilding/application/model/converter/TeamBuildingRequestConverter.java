package com.blackcompany.eeos.teamBuilding.application.model.converter;

import com.blackcompany.eeos.common.support.converter.AbstractDtoConverter;
import com.blackcompany.eeos.teamBuilding.application.dto.CommandTeamBuildingRequest;
import com.blackcompany.eeos.teamBuilding.application.model.TeamBuildingModel;
import org.springframework.stereotype.Component;

@Component
public class TeamBuildingRequestConverter
		implements AbstractDtoConverter<CommandTeamBuildingRequest, TeamBuildingModel> {
	@Override
	public TeamBuildingModel from(CommandTeamBuildingRequest commandTeamBuildingRequest) {
		return TeamBuildingModel.builder()
				.title(commandTeamBuildingRequest.getTitle())
				.content(commandTeamBuildingRequest.getContent())
				.maxTeamSize(commandTeamBuildingRequest.getMaxTeamSize())
				.build();
	}
}
