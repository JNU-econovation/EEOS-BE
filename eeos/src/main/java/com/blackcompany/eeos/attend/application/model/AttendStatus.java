package com.blackcompany.eeos.attend.application.model;

public enum AttendStatus {
	PARTICIPATE("participate"),
	NON_PARITICPATE("non-participate"),
	NONE("none");

	private final String status;

	AttendStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return this.status;
	}
}
