package com.blackcompany.eeos.calendar.application.model;

import static org.junit.jupiter.api.Assertions.*;

import com.blackcompany.eeos.calendar.application.exception.NotFoundCalendarTypeException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CalendarTypeTest {

	@Test
	@DisplayName("EVENT 타입은 5, 3, 1일 전에 알림을 보낸다")
	void event_notification_days() {
		// given
		CalendarType type = CalendarType.EVENT;

		// when
		List<Integer> days = type.getNotificationDays();

		// then
		assertEquals(List.of(5, 3, 1), days);
	}

	@Test
	@DisplayName("PRESENTATION 타입은 1일 전에만 알림을 보낸다")
	void presentation_notification_days() {
		// given
		CalendarType type = CalendarType.PRESENTATION;

		// when
		List<Integer> days = type.getNotificationDays();

		// then
		assertEquals(List.of(1), days);
	}

	@Test
	@DisplayName("ETC 타입은 3, 1일 전에 알림을 보낸다")
	void etc_notification_days() {
		// given
		CalendarType type = CalendarType.ETC;

		// when
		List<Integer> days = type.getNotificationDays();

		// then
		assertEquals(List.of(3, 1), days);
	}

	@Test
	@DisplayName("이름으로 CalendarType을 찾을 수 있다")
	void find_by_name() {
		// given
		String name = "event";

		// when
		CalendarType type = CalendarType.findByName(name);

		// then
		assertEquals(CalendarType.EVENT, type);
	}

	@Test
	@DisplayName("대소문자 구분 없이 CalendarType을 찾을 수 있다")
	void find_by_name_case_insensitive() {
		// given
		String name = "EVENT";

		// when
		CalendarType type = CalendarType.findByName(name);

		// then
		assertEquals(CalendarType.EVENT, type);
	}

	@Test
	@DisplayName("존재하지 않는 이름으로 찾으면 예외가 발생한다")
	void find_by_name_not_found() {
		// given
		String name = "unknown";

		// when & then
		assertThrows(NotFoundCalendarTypeException.class, () -> CalendarType.findByName(name));
	}
}
