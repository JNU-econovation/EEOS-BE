package com.blackcompany.eeos.program.application.model;

import com.blackcompany.eeos.program.application.exception.NotFoundProgramStatusException;
import java.util.Arrays;

public enum ProgramStatus {
	/** 프로그램의 날짜가 조회 시의 날짜와 같거나 이후일 때 */
	ACTIVE("active"),
	/** 프로그램의 날짜가 조회 시의 날짜 이전일 때 */
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

	public String getStatus() {
		return status;
	}
}
