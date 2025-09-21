package com.blackcompany.eeos.program.persistence;

import com.blackcompany.eeos.program.application.model.SemesterPeriodModel;
import com.blackcompany.eeos.program.application.repository.SemesterPeriodRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SemesterPeriodRepositoryImpl implements SemesterPeriodRepository {
	private final JpaSemesterPeriodRepository jpaSemesterPeriodRepository;

	@Override
	public Optional<SemesterPeriodModel> getSemesterPeriod() {
		return jpaSemesterPeriodRepository.findTopByOrderByCreatedDateDesc().map(this::toModel);
	}

	@Override
	public SemesterPeriodModel updateSemesterPeriod(SemesterPeriodModel model) {
		SemesterPeriodEntity entity = toEntity(model);
		SemesterPeriodEntity savedEntity = jpaSemesterPeriodRepository.save(entity);
		return toModel(savedEntity);
	}

	private SemesterPeriodModel toModel(SemesterPeriodEntity entity) {
		return SemesterPeriodModel.builder()
				.startDate(entity.getStartDate())
				.endDate(entity.getEndDate())
				.build();
	}

	private SemesterPeriodEntity toEntity(SemesterPeriodModel model) {
		return SemesterPeriodEntity.builder()
				.startDate(model.getStartDate())
				.endDate(model.getEndDate())
				.build();
	}
}
