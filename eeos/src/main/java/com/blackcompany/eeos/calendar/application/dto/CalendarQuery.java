package com.blackcompany.eeos.calendar.application.dto;

public record CalendarQuery(
        Integer year,
        Integer month,
        Integer date,
        Integer duration
) {
}
