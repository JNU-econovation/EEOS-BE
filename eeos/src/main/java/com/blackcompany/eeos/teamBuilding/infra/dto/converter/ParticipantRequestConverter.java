package com.blackcompany.eeos.teamBuilding.infra.dto.converter;

import com.blackcompany.eeos.target.application.model.TeamBuildingTargetModel;
import com.blackcompany.eeos.teamBuilding.infra.dto.ParticipantRequest;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class ParticipantRequestConverter {
	public ParticipantRequest from(List<TeamBuildingTargetModel> models, int maxTeamSize) {
		return ParticipantRequest.builder()
				.maxTeamSize(maxTeamSize)
				.participantResponses(
						models.stream().map(ParticipantRequestConverter::from).collect(Collectors.toList()))
				.build();
	}

	private static ParticipantRequest.EachParticipantResponse from(TeamBuildingTargetModel model) {
		return ParticipantRequest.EachParticipantResponse.builder()
				.memberId(String.valueOf(model.getMemberId()))
				.sentence(model.getContent())
				.build();
	}
}
