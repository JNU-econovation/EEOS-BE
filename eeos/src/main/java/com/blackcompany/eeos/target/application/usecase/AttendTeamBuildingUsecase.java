package com.blackcompany.eeos.target.application.usecase;

import com.blackcompany.eeos.target.application.dto.AttendTeamBuildingRequest;

public interface AttendTeamBuildingUsecase {
	void create(Long memberId, AttendTeamBuildingRequest request);
}
