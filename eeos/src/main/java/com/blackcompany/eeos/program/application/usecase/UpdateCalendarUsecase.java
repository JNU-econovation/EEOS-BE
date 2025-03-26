package com.blackcompany.eeos.program.application.usecase;

import com.blackcompany.eeos.program.application.dto.request.CalendarApplicationCommand;
import com.blackcompany.eeos.program.application.dto.response.CalendarPeriodApplicationQuery;

public interface UpdateCalendarUsecase {
	CalendarPeriodApplicationQuery updateCalendar(CalendarApplicationCommand command);
}
