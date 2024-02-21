package com.blackcompany.eeos.teamBuilding.application.usecase;

import com.blackcompany.eeos.common.support.dto.AbstractResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class QueryTeamBuildingResponse implements AbstractResponseDto {
	private String title;
	private String content;
	private String accessRight;
}
