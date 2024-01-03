package com.blackcompany.eeos.member.application.model;

import com.blackcompany.eeos.auth.application.exception.NotFoundOauthServerException;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum ActiveStatus {
	AM("am"),
	CM("cm"),
	RM("rm");
	private final String status;

	ActiveStatus(String status) {
		this.status = status;
	}

	public static ActiveStatus find(String status) {
		return Arrays.stream(values())
				.filter(activeStatus -> activeStatus.status.equals(status))
				.findAny()
				.orElseThrow(() -> new NotFoundOauthServerException(status));
	}

	public String getStatus() {
		return status;
	}
}
