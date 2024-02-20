package com.blackcompany.eeos.teamBuilding.application.model.converter;

import com.blackcompany.eeos.teamBuilding.persistence.TeamBuildingResultEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class TeamBuildingResultConverter {
	public TeamBuildingResultEntity from(List<String> memberIds, Long teamBuildinId) {
		return TeamBuildingResultEntity.builder()
				.teamBuildingId(teamBuildinId)
				.memberIds(memberIds.stream().map(Long::parseLong).collect(Collectors.toList()))
				.build();
	}
}
