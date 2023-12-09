package com.blackcompany.eeos.program.application.model;

import com.blackcompany.eeos.program.application.exception.NotFoundProgramStatusException;
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
				.orElseThrow(NotFoundProgramStatusException::new);
	}
}
