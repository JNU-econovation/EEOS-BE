package com.blackcompany.eeos.teamBuilding.application.usecase;

import com.blackcompany.eeos.teamBuilding.application.dto.ResultTeamBuildingResponse;

public interface GetResultTeamBuildingUsecase {
	/**
	 * 팀빌딩 결과를 조회한다.
	 *
	 * @param memberId 팀빌딩 작성자
	 */
	ResultTeamBuildingResponse getResult(Long memberId);
}
