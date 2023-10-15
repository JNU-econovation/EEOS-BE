package com.blackcompany.eeos.program.persistence;

public enum EventStatus {
	ING("ing"),
	END("end");

	private String status;

	EventStatus(String status) {
		this.status = status;
	}
}
