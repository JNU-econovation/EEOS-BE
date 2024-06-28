package com.blackcompany.eeos.team.application.usecase;

import com.blackcompany.eeos.team.application.dto.QueryTeamsResponse;

public interface GetTeamsByActiveStatus {
	QueryTeamsResponse execute(String programId);
}
