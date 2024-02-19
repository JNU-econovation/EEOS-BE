package com.blackcompany.eeos.target.persistence;

import com.blackcompany.eeos.target.application.exception.NotFoundTeamBuildingInputStatus;
import java.util.Arrays;

public enum TeamBuildingInputStatus {
	COMPLETE("complete"),
	INCOMPLETE("incomplete");

	private final String status;

	TeamBuildingInputStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public static TeamBuildingInputStatus find(String status) {
		return Arrays.stream(values())
				.filter(teamBuildingInputStatus -> teamBuildingInputStatus.getStatus().equals(status))
				.findAny()
				.orElseThrow(() -> new NotFoundTeamBuildingInputStatus(status));
	}
}
