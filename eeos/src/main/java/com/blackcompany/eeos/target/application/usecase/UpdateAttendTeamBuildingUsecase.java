package com.blackcompany.eeos.target.application.usecase;

import com.blackcompany.eeos.target.application.dto.AttendTeamBuildingRequest;

public interface UpdateAttendTeamBuildingUsecase {
	void update(Long memberId, AttendTeamBuildingRequest request);
}
