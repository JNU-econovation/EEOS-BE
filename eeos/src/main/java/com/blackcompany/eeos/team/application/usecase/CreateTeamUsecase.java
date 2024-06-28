package com.blackcompany.eeos.team.application.usecase;

import com.blackcompany.eeos.team.application.dto.CreateTeamRequest;
import com.blackcompany.eeos.team.application.dto.CreateTeamResponse;

public interface CreateTeamUsecase {
	CreateTeamResponse create(Long memberId, CreateTeamRequest request);
}
