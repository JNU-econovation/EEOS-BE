package com.blackcompany.eeos.program.application.usecase;

import com.blackcompany.eeos.program.application.dto.request.SemesterPeriodApplicationCommand;
import com.blackcompany.eeos.program.application.dto.response.SemesterPeriodApplicationQuery;

public interface UpdateSemesterPeriodUsecase {
	SemesterPeriodApplicationQuery updateSemesterPeriod(SemesterPeriodApplicationCommand command);
}
