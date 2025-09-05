package com.blackcompany.eeos.calendar.application.usecase;

import com.blackcompany.eeos.calendar.application.dto.CalendarQuery;
import com.blackcompany.eeos.calendar.application.dto.CalendarResponse;
import java.util.List;

public interface GetCalendarUsecase {

	List<CalendarResponse> getCalendar(CalendarQuery query);
}
