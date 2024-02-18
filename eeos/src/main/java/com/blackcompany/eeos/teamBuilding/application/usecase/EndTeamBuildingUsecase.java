package com.blackcompany.eeos.teamBuilding.application.usecase;

public interface EndTeamBuildingUsecase {
	/**
	 * 팀빌딩을 종료한다.
	 *
	 * @param memberId 팀빌딩 작성자
	 */
	void delete(Long memberId);
}
