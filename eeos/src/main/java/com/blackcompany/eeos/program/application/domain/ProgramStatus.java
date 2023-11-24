package com.blackcompany.eeos.program.application.domain;

public enum ProgramStatus {
	ACTIVE("active"),
	END("end");

	private final String status;

	ProgramStatus(String status) {
		this.status = status;
	}

	public static boolean isSameStatus(String source, ProgramStatus expected) {
		return expected.status.equals(source);
	}
}
