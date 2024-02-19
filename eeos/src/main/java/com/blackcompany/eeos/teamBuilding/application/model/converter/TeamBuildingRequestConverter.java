package com.blackcompany.eeos.teamBuilding.application.model.converter;

import com.blackcompany.eeos.common.support.converter.AbstractDtoConverter;
import com.blackcompany.eeos.teamBuilding.application.dto.CommandTeamBuildingRequest;
import com.blackcompany.eeos.teamBuilding.application.model.TeamBuildingModel;
import com.blackcompany.eeos.teamBuilding.persistence.TeamBuildingStatus;
import org.springframework.stereotype.Component;

@Component
public class TeamBuildingRequestConverter
		implements AbstractDtoConverter<CommandTeamBuildingRequest, TeamBuildingModel> {
	@Override
	public TeamBuildingModel from(CommandTeamBuildingRequest source) {
		return TeamBuildingModel.builder()
				.title(source.getTitle())
				.content(source.getContent())
				.maxTeamSize(source.getMaxTeamSize())
				.status(TeamBuildingStatus.PROGRESS.getStatus())
				.build();
	}

	public TeamBuildingModel from(
			Long memberId, CommandTeamBuildingRequest source, TeamBuildingStatus status) {
		return TeamBuildingModel.builder()
				.title(source.getTitle())
				.content(source.getContent())
				.maxTeamSize(source.getMaxTeamSize())
				.status(status.getStatus())
				.memberId(memberId)
				.build();
	}
}
