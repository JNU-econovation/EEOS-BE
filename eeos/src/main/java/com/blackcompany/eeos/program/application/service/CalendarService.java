package com.blackcompany.eeos.program.application.service;

import com.blackcompany.eeos.program.application.dto.request.CalendarApplicationCommand;
import com.blackcompany.eeos.program.application.dto.response.CalendarPeriodApplicationQuery;
import com.blackcompany.eeos.program.application.model.CalendarModel;
import com.blackcompany.eeos.program.application.repository.CalendarRepository;
import com.blackcompany.eeos.program.application.support.CalendarProvider;
import com.blackcompany.eeos.program.application.usecase.GetCalendarUsecase;
import com.blackcompany.eeos.program.application.usecase.UpdateCalendarUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CalendarService implements GetCalendarUsecase, UpdateCalendarUsecase {
	private final CalendarRepository calendarRepository;
	private final CalendarProvider calendarProvider;

	@Override
	public CalendarPeriodApplicationQuery getCalendar() {
		CalendarModel model = calendarProvider.getCalendar();
		return new CalendarPeriodApplicationQuery(model.getStartDate(), model.getEndDate());
	}

	@Override
	@Transactional
	public CalendarPeriodApplicationQuery updateCalendar(CalendarApplicationCommand command) {
		CalendarModel model =
				calendarRepository.updateCalendar(
						new CalendarModel(command.startDate(), command.endDate()));
		return new CalendarPeriodApplicationQuery(model.getStartDate(), model.getEndDate());
	}
}
