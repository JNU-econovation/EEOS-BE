package com.blackcompany.eeos.teamBuilding.application.dto;

import com.blackcompany.eeos.common.support.dto.AbstractRequestDto;
import java.util.List;

public interface CommandTeamBuildingRequest extends AbstractRequestDto {
	String getTitle();

	String getContent();

	int getMaxTeamSize();

	List<TeamBuildingMember> getMembers();
}
