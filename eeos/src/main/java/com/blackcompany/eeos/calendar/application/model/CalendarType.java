package com.blackcompany.eeos.calendar.application.model;

import com.blackcompany.eeos.calendar.application.exception.NotFoundCalendarTypeException;
import java.util.Arrays;
import java.util.List;

public enum CalendarType {
	EVENT(List.of(5, 3, 1)),
	PRESENTATION(List.of(1)),
	ETC(List.of(3, 1));

	private final List<Integer> notificationDays;

	CalendarType(List<Integer> notificationDays) {
		this.notificationDays = notificationDays;
	}

	public static CalendarType findByName(String name) {
		return Arrays.stream(CalendarType.values())
				.filter(calendarType -> calendarType.name().equalsIgnoreCase(name))
				.findFirst()
				.orElseThrow(NotFoundCalendarTypeException::new);
	}

	public List<Integer> getNotificationDays() {
		return notificationDays;
	}
}
