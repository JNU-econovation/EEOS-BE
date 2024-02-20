package com.blackcompany.eeos.teamBuilding.persistence;

import com.blackcompany.eeos.teamBuilding.application.exception.NotFoundTeamBuildingStatusException;
import java.util.Arrays;

/** 팀빌딩의 상태를 나타낸다 */
public enum TeamBuildingStatus {
	/** 진행 중 */
	PROGRESS("progress"),
	/** 팀빌딩 결과 요청 완료 */
	COMPLETE("completed"),
	/** 팀빌딩 결과 종료 */
	END("end");

	private final String status;

	TeamBuildingStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public static TeamBuildingStatus find(String status) {
		return Arrays.stream(values())
				.filter(teamBuildingStatus -> teamBuildingStatus.getStatus().equals(status))
				.findAny()
				.orElseThrow(() -> new NotFoundTeamBuildingStatusException(status));
	}

	public static boolean validateSame(String status, TeamBuildingStatus targetStatus) {
		return status.equals(targetStatus.getStatus());
	}
}
