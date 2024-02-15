package com.blackcompany.eeos.teamBuilding.application.dto;

import com.blackcompany.eeos.common.support.dto.AbstractRequestDto;

public interface CommandTeamBuildingRequest extends AbstractRequestDto {
	String getTitle();

	String getContent();

	int getMaxTeamSize();
}
