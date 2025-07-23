package com.blackcompany.eeos.auth.application.model;

import lombok.Getter;

@Getter
public enum Role {
	ROLE_ADMIN("ADMIN"),
	ROLE_USER("USER");

	private String role;

	Role(String role) {
		this.role = role;
	}
}
