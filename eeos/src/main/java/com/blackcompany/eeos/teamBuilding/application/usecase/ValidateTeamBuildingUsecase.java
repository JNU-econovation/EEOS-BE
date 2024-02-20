package com.blackcompany.eeos.teamBuilding.application.usecase;

import com.blackcompany.eeos.teamBuilding.application.dto.ValidateTeamBuildingResponse;

public interface ValidateTeamBuildingUsecase {
	/**
	 * 팀빌딩이 요청한 정보가 가능한 상태인지 검증한다.
	 *
	 * @param memberId 팀빌딩 작성자
	 */
	ValidateTeamBuildingResponse validate(Long memberId, String status);
}
