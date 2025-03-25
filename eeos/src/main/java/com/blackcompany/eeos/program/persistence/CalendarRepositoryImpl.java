package com.blackcompany.eeos.program.persistence;

import com.blackcompany.eeos.program.application.model.CalendarModel;
import com.blackcompany.eeos.program.application.repository.CalendarRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CalendarRepositoryImpl implements CalendarRepository {
	private final JpaCalendarRepository jpaCalendarRepository;

	@Override
	public Optional<CalendarModel> getCalendar() {
		return jpaCalendarRepository.findTopByOrderByCreatedDateDesc().map(this::toModel);
	}

	@Override
	public CalendarModel updateCalendar(CalendarModel model) {
		CalendarEntity entity = toEntity(model);
		CalendarEntity savedEntity = jpaCalendarRepository.save(entity);
		return toModel(savedEntity);
	}

	private CalendarModel toModel(CalendarEntity entity) {
		return CalendarModel.builder()
				.startDate(entity.getStartDate())
				.endDate(entity.getEndDate())
				.build();
	}

	private CalendarEntity toEntity(CalendarModel model) {
		return CalendarEntity.builder()
				.startDate(model.getStartDate())
				.endDate(model.getEndDate())
				.build();
	}
}
