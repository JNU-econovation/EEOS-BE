package com.blackcompany.eeos.teamBuilding.application.dto.converter;

import com.blackcompany.eeos.teamBuilding.application.dto.EachMemberResponse;
import com.blackcompany.eeos.teamBuilding.application.dto.ResultTeamBuildingResponse;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ResultTeamBuildingResponseConverter {
	public ResultTeamBuildingResponse from(
			String accessRight, List<List<EachMemberResponse>> results) {
		return ResultTeamBuildingResponse.builder().accessRights(accessRight).results(results).build();
	}
}
