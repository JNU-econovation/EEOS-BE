package com.blackcompany.eeos.program.application.model;

public enum EventStatus {
	ING("ing"),
	END("end");

	private final String status;

	EventStatus(String status) {
		this.status = status;
	}
}
