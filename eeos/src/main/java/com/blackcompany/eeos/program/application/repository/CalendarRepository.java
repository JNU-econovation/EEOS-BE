package com.blackcompany.eeos.program.application.repository;

import com.blackcompany.eeos.program.application.model.CalendarModel;
import java.util.Optional;

public interface CalendarRepository {
	Optional<CalendarModel> getCalendar();

	CalendarModel updateCalendar(CalendarModel model);
}
