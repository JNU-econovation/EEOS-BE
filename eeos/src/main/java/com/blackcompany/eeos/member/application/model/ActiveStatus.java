package com.blackcompany.eeos.member.application.model;

public enum ActiveStatus {
	AM("am"),
	CM("cm"),
	RM("rm");

	ActiveStatus(String status) {
		this.status = status;
	}

	private final String status;
}
