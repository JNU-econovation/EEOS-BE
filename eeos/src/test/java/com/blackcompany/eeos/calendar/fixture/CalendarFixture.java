package com.blackcompany.eeos.calendar.fixture;

import com.blackcompany.eeos.calendar.application.dto.CalendarCreateCommand;
import com.blackcompany.eeos.calendar.application.dto.CalendarQuery;
import com.blackcompany.eeos.calendar.application.dto.CalendarUpdateCommand;
import com.blackcompany.eeos.calendar.application.model.CalendarModel;
import com.blackcompany.eeos.calendar.application.model.CalendarType;
import java.time.LocalDateTime;

public class CalendarFixture {

	private static final LocalDateTime DEFAULT_START = LocalDateTime.of(2024, 1, 15, 10, 0);
	private static final LocalDateTime DEFAULT_END = LocalDateTime.of(2024, 1, 15, 12, 0);
	private static final Long DEFAULT_START_MILLIS = 1705312800000L;
	private static final Long DEFAULT_END_MILLIS = 1705320000000L;

	public static CalendarModel 캘린더_모델(Long id, Long writerId) {
		return CalendarModel.load(
				id, "테스트 일정", DEFAULT_START, DEFAULT_END, CalendarType.ETC, "https://example.com", writerId);
	}

	public static CalendarModel 캘린더_모델_이벤트(Long id, Long writerId) {
		return CalendarModel.load(
				id,
				"이벤트 일정",
				DEFAULT_START,
				DEFAULT_END,
				CalendarType.EVENT,
				"https://example.com",
				writerId);
	}

	public static CalendarModel 캘린더_모델_발표(Long id, Long writerId) {
		return CalendarModel.load(
				id,
				"발표 일정",
				DEFAULT_START,
				DEFAULT_END,
				CalendarType.PRESENTATION,
				"https://example.com",
				writerId);
	}

	public static CalendarCreateCommand 캘린더_생성_커맨드() {
		return new CalendarCreateCommand(
				"테스트 일정", "https://example.com", "ETC", DEFAULT_START_MILLIS, DEFAULT_END_MILLIS);
	}

	public static CalendarCreateCommand 캘린더_생성_커맨드_이벤트() {
		return new CalendarCreateCommand(
				"이벤트 일정", "https://example.com", "EVENT", DEFAULT_START_MILLIS, DEFAULT_END_MILLIS);
	}

	public static CalendarCreateCommand 캘린더_생성_커맨드_잘못된_기간() {
		return new CalendarCreateCommand(
				"테스트 일정", "https://example.com", "ETC", DEFAULT_END_MILLIS, DEFAULT_START_MILLIS);
	}

	public static CalendarUpdateCommand 캘린더_수정_커맨드() {
		return new CalendarUpdateCommand(
				"수정된 일정", "https://updated.com", "ETC", DEFAULT_START_MILLIS, DEFAULT_END_MILLIS);
	}

	public static CalendarQuery 캘린더_조회_쿼리_월별() {
		return new CalendarQuery(2024, 1, null, null);
	}

	public static CalendarQuery 캘린더_조회_쿼리_일별() {
		return new CalendarQuery(2024, 1, 15, 7);
	}
}
