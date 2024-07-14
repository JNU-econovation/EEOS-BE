package com.blackcompany.eeos.program.application.model;

import lombok.Getter;

@Getter
public enum ProgramAttendMode {

	/** 참석 수합 중 */
	ATTEND("attend"),
	/** 지각 수합 중 */
	LATE("late"),
	/** 어떠한 상태도 아닐 때 */
	END("end");

	private String mode;

	ProgramAttendMode(String mode) {
		this.mode = mode;
	}
}
