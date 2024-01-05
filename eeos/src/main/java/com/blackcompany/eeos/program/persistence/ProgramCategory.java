package com.blackcompany.eeos.program.persistence;

import com.blackcompany.eeos.program.application.exception.NotFoundProgramCategoryException;
import java.util.Arrays;

public enum ProgramCategory {
	/** 주간발표 */
	WEEKLY("weekly"),
	/** 회장단 */
	PRESIDENT_TEAM("presidentTeam"),
	/** 행사부 */
	EVENT_TEAM("eventTeam"),
	/** 기타 */
	ETC("etc");

	private final String category;

	ProgramCategory(String category) {
		this.category = category;
	}

	public String getCategory() {
		return category;
	}

	public static ProgramCategory find(String category) {
		return Arrays.stream(values())
				.filter(programCategory -> programCategory.getCategory().equals(category))
				.findAny()
				.orElseThrow(() -> new NotFoundProgramCategoryException(category));
	}
}
