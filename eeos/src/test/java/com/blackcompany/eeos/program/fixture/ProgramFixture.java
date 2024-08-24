package com.blackcompany.eeos.program.fixture;

import com.blackcompany.eeos.common.utils.DateConverter;
import com.blackcompany.eeos.program.application.model.ProgramModel;
import com.blackcompany.eeos.program.persistence.ProgramCategory;
import com.blackcompany.eeos.program.persistence.ProgramType;
import java.time.LocalDate;

public class ProgramFixture {
	private static final String GITHUB_URL = "https://github.com/JNU-econovation/";

	public static ProgramModel 프로그램_모델(LocalDate date) {
		return 프로그램_모델(date, ProgramCategory.WEEKLY, ProgramType.DEMAND, 1L);
	}

	public static ProgramModel 프로그램_모델(LocalDate date, Long writerId) {
		return 프로그램_모델(date, ProgramCategory.WEEKLY, ProgramType.DEMAND, writerId);
	}

	public static ProgramModel 프로그램_모델(LocalDate date, ProgramType programType, Long writerId) {
		return 프로그램_모델(date, ProgramCategory.WEEKLY, programType, writerId);
	}

	public static ProgramModel 프로그램_모델(
			LocalDate date, ProgramCategory category, ProgramType type, Long writerId) {
		return ProgramModel.builder()
				.title(writerId.toString())
				.content(writerId.toString())
				.programDate(DateConverter.toEpochSecond(date))
				.programCategory(category)
				.programType(type)
				.writer(writerId)
				.githubUrl(GITHUB_URL + writerId)
				.build();
	}
}
