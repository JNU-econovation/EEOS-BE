package com.blackcompany.eeos.program.application.repository;

import com.blackcompany.eeos.program.application.model.SemesterPeriodModel;
import java.util.Optional;

public interface SemesterPeriodRepository {
	Optional<SemesterPeriodModel> getSemesterPeriod();

	SemesterPeriodModel updateSemesterPeriod(SemesterPeriodModel model);
}
