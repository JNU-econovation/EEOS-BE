package com.blackcompany.eeos.program.application.service;

import com.blackcompany.eeos.program.application.dto.request.SemesterPeriodApplicationCommand;
import com.blackcompany.eeos.program.application.dto.response.SemesterPeriodApplicationQuery;
import com.blackcompany.eeos.program.application.model.SemesterPeriodModel;
import com.blackcompany.eeos.program.application.repository.SemesterPeriodRepository;
import com.blackcompany.eeos.program.application.support.SemesterPeriodProvider;
import com.blackcompany.eeos.program.application.usecase.GetSemesterPeriodUsecase;
import com.blackcompany.eeos.program.application.usecase.UpdateSemesterPeriodUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SemesterPeriodService implements GetSemesterPeriodUsecase, UpdateSemesterPeriodUsecase {
	private final SemesterPeriodRepository semesterPeriodRepository;
	private final SemesterPeriodProvider semesterPeriodProvider;

	@Override
	public SemesterPeriodApplicationQuery getSemesterPeriod() {
		SemesterPeriodModel model = semesterPeriodProvider.getSemesterPeriod();
		return new SemesterPeriodApplicationQuery(model.getStartDate(), model.getEndDate());
	}

	@Override
	@Transactional
	public SemesterPeriodApplicationQuery updateSemesterPeriod(SemesterPeriodApplicationCommand command) {
		SemesterPeriodModel model =
				semesterPeriodRepository.updateSemesterPeriod(
						new SemesterPeriodModel(command.startDate(), command.endDate()));
		return new SemesterPeriodApplicationQuery(model.getStartDate(), model.getEndDate());
	}
}
