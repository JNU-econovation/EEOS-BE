package com.blackcompany.eeos.program.application.support;

import com.blackcompany.eeos.program.application.model.CalendarModel;
import com.blackcompany.eeos.program.application.repository.CalendarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CalendarProvider {
	private final CalendarRepository calendarRepository;

	public CalendarModel getCalendar() {
		return calendarRepository.getCalendar().orElse(new CalendarModel());
	}
}
