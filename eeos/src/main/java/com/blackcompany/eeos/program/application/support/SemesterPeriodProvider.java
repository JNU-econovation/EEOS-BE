package com.blackcompany.eeos.program.application.support;

import com.blackcompany.eeos.program.application.model.SemesterPeriodModel;
import com.blackcompany.eeos.program.application.repository.SemesterPeriodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SemesterPeriodProvider {
	private final SemesterPeriodRepository semesterPeriodRepository;

	public SemesterPeriodModel getSemesterPeriod() {
		return semesterPeriodRepository.getSemesterPeriod().orElse(new SemesterPeriodModel());
	}
}
