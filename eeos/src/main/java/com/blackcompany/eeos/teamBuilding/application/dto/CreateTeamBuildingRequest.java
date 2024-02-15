package com.blackcompany.eeos.teamBuilding.application.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CreateTeamBuildingRequest implements CommandTeamBuildingRequest {
	private String title;
	private String content;
	private int maxTeamSize;
	private List<TeamBuildingMember> members;
}
