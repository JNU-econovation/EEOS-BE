package com.blackcompany.eeos.calendar.application.repository;

import com.blackcompany.eeos.calendar.application.model.CalendarModel;
import java.time.LocalDateTime;
import java.util.List;

public interface CalendarRepository {

	Long save(CalendarModel calendar);

	CalendarModel findById(Long id);

	List<CalendarModel> findByBetweenDate(LocalDateTime startAt, LocalDateTime endAt);

	void delete(Long id);
}
