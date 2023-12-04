package com.blackcompany.eeos.program.application.domain;

import com.blackcompany.eeos.attend.application.exception.NotFoundAttendStatusException;
import java.util.Arrays;

public enum ProgramStatus {
	ACTIVE("active"),
	END("end");

	private final String status;

	ProgramStatus(String status) {
		this.status = status;
	}

	public static ProgramStatus getStatus(String source) {
		return Arrays.stream(ProgramStatus.values())
				.filter(status -> status.status.equals(source))
				.findAny()
				.orElseThrow(()-> new NotFoundAttendStatusException(source));
	}
}
