package com.blackcompany.eeos.program.infra.api.slack.chat.model;

import lombok.Getter;

@Getter
public enum BlockTypes {
	SECTION("section");

	private String type;

	BlockTypes(String type) {
		this.type = type;
	}
}
