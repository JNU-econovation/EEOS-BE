package com.blackcompany.eeos.calendar.application.model;

import com.blackcompany.eeos.calendar.application.exception.NotFoundCalendarTypeException;
import java.util.Arrays;

public enum CalendarType {

    EVENT,
    PRESENTATION,
    ETC;

    CalendarType(){}

    public static CalendarType findByName(String name) {
        return Arrays.stream(CalendarType.values())
                .filter(calendarType -> calendarType.name().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(NotFoundCalendarTypeException::new);
    }

}
