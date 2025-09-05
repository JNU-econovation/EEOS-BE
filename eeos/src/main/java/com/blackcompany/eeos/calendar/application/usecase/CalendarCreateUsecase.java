package com.blackcompany.eeos.calendar.application.usecase;

import com.blackcompany.eeos.calendar.application.dto.CalendarCreateCommand;

public interface CalendarCreateUsecase {

    Long create(CalendarCreateCommand command);

}
