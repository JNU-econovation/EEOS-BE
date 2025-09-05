package com.blackcompany.eeos.calendar.application.repository;

import com.blackcompany.eeos.calendar.application.model.CalendarModel;

public interface CalendarRepository {

    Long save(CalendarModel calendar);

    CalendarModel findById(Long id);

}
