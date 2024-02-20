package com.blackcompany.eeos.teamBuilding.application.model;

import lombok.Getter;

@Getter
public enum TeamBuildingAccessRights {
	/** 읽기, 수정 권한 있음 */
	EDIT("edit"),
	/** 읽기 권한 있음 */
	READ_ONLY("read_only");

	private final String accessRight;

	TeamBuildingAccessRights(String accessRight) {
		this.accessRight = accessRight;
	}
}
