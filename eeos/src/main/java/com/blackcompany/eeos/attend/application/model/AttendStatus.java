package com.blackcompany.eeos.attend.application.model;

import com.blackcompany.eeos.attend.application.exception.NotFoundAttendException;
import java.util.Arrays;

public enum AttendStatus {
	PARTICIPATE("participate"),
	NON_PARTICPATE("non-participate"),
	NONE("none");

	private final String status;

	AttendStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return this.status;
	}

	public static AttendStatus findStatus(String status) {
		return Arrays.stream(AttendStatus.values())
				.filter(attendStatus -> attendStatus.getStatus().equals(status))
				.findAny()
				.orElseThrow(NotFoundAttendException::new);
	}

	public boolean isEqualsStatus(String status) {
		return this.getStatus().equals(status);
	}
}
