package com.blackcompany.eeos.teamBuilding.application.model;

public enum Accessibility {
	CREATABLE("creatable"),
	NONCREATABLE("noncreatable"),
	JOINABLE("joinable"),
	NONJOINABLE("nonjoinable");

	private final String status;

	Accessibility(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}
}
