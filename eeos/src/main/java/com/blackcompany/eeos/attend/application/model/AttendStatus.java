package com.blackcompany.eeos.attend.application.model;

import com.blackcompany.eeos.attend.application.exception.NotFoundAttendStatusException;
import java.util.Arrays;

public enum AttendStatus {
	ATTEND("attend"),
	ABSENT("absent"),
	NONE("none");

	private final String status;

	AttendStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public static AttendStatus findByAttendStatus(String status) {
		return Arrays.stream(AttendStatus.values())
				.filter(attendStatus -> attendStatus.getStatus().equals(status))
				.findAny()
				.orElseThrow(() -> new NotFoundAttendStatusException(status));
	}

	public static boolean isSameAttendStatus(String source, AttendStatus actualStatus) {
		return actualStatus.getStatus().equals(source);
	}
}
