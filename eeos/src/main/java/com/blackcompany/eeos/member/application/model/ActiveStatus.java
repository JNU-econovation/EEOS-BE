package com.blackcompany.eeos.member.application.model;

import com.blackcompany.eeos.auth.application.exception.NotFoundOauthServerException;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum ActiveStatus {
	ALL("all"),
	AM("am"),
	CM("cm"),
	RM("rm"),
	OB("ob");
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

	public boolean isAll() {
		return status.equals(ActiveStatus.ALL.getStatus());
	}

	public static boolean isSame(String activeStatus, ActiveStatus status) {
		return status.getStatus().equals(activeStatus);
	}
}
