package com.blackcompany.eeos.teamBuilding.application.usecase;

public interface CompleteTeamBuildingUsecase {
	/**
	 * 팀빌딩 완료를 요청한다.
	 *
	 * @param memberId 팀빌딩 작성자
	 */
	void complete(Long memberId);
}
