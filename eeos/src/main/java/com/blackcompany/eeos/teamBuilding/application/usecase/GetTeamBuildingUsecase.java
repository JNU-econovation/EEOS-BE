package com.blackcompany.eeos.teamBuilding.application.usecase;

public interface GetTeamBuildingUsecase {
	QueryTeamBuildingResponse getTeamBuilding(Long memberId, String status);
}
