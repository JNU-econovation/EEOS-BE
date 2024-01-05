package com.blackcompany.eeos.program.persistence;

import com.blackcompany.eeos.program.application.exception.NotFoundProgramTypeException;
import java.util.Arrays;

public enum ProgramType {
	DEMAND("demand"),
	NOTIFICATION("notification"),
	;

	private final String type;

	ProgramType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public static ProgramType find(String type) {
		return Arrays.stream(values())
				.filter(programType -> programType.getType().equals(type))
				.findAny()
				.orElseThrow(() -> new NotFoundProgramTypeException(type));
	}
}
