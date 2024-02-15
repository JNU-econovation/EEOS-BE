package com.blackcompany.eeos.teamBuilding.application.usecase;

import com.blackcompany.eeos.teamBuilding.application.dto.CreateTeamBuildingRequest;

public interface CreateTeamBuildingUsecase {
	/**
	 * 팀빌딩을 저장시킨다.
	 *
	 * @param request 팀빌딩 생성자 id
	 * @param request 팀빌딩 저장을 하기 위한 request 객체
	 * @return 프로그램 식별 id 전달
	 */
	void create(Long memberId, CreateTeamBuildingRequest request);
}
