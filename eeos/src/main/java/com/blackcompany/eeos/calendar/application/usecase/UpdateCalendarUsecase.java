package com.blackcompany.eeos.calendar.application.usecase;

import com.blackcompany.eeos.calendar.application.dto.CalendarUpdateCommand;

public interface UpdateCalendarUsecase {

	Long update(Long calendarId, CalendarUpdateCommand command);
}
