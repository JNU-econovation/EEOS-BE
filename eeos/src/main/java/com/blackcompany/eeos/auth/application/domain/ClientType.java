package com.blackcompany.eeos.auth.application.domain;

public enum ClientType {
	WEB(true),
	APP(false);

	private final boolean confidential;

	ClientType(boolean confidential) {
		this.confidential = confidential;
	}

	public boolean isConfidential() {
		return confidential;
	}
}
