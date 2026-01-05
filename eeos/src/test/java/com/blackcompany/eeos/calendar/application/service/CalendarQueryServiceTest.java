package com.blackcompany.eeos.calendar.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.blackcompany.eeos.calendar.application.dto.CalendarQuery;
import com.blackcompany.eeos.calendar.application.dto.CalendarResponse;
import com.blackcompany.eeos.calendar.application.model.CalendarModel;
import com.blackcompany.eeos.calendar.application.model.CalendarType;
import com.blackcompany.eeos.calendar.application.repository.CalendarRepository;
import com.blackcompany.eeos.calendar.fixture.CalendarFixture;
import com.blackcompany.eeos.member.application.repository.MemberRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CalendarQueryServiceTest {

	@Mock CalendarRepository calendarRepository;
	@Mock MemberRepository memberRepository;
	@InjectMocks CalendarQueryService calendarQueryService;

	private static final Long MEMBER_ID = 1L;
	private static final Long CALENDAR_ID = 1L;

	@Test
	@DisplayName("월별 캘린더를 조회할 수 있다")
	void get_calendar_by_month() {
		// given
		CalendarQuery query = CalendarFixture.캘린더_조회_쿼리_월별();
		CalendarModel calendar = CalendarFixture.캘린더_모델(CALENDAR_ID, MEMBER_ID);
		List<CalendarModel> calendars = List.of(calendar);

		when(calendarRepository.findByBetweenDate(any(LocalDateTime.class), any(LocalDateTime.class)))
				.thenReturn(calendars);
		when(memberRepository.findNameById(MEMBER_ID)).thenReturn("테스트 작성자");

		// when
		List<CalendarResponse> result = calendarQueryService.getCalendar(query);

		// then
		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals("테스트 일정", result.get(0).title());
		assertEquals("테스트 작성자", result.get(0).writer());
	}

	@Test
	@DisplayName("일별 캘린더를 조회할 수 있다")
	void get_calendar_by_date() {
		// given
		CalendarQuery query = CalendarFixture.캘린더_조회_쿼리_일별();
		CalendarModel calendar = CalendarFixture.캘린더_모델(CALENDAR_ID, MEMBER_ID);
		List<CalendarModel> calendars = List.of(calendar);

		when(calendarRepository.findByBetweenDate(any(LocalDateTime.class), any(LocalDateTime.class)))
				.thenReturn(calendars);
		when(memberRepository.findNameById(MEMBER_ID)).thenReturn("테스트 작성자");

		// when
		List<CalendarResponse> result = calendarQueryService.getCalendar(query);

		// then
		assertNotNull(result);
		assertEquals(1, result.size());
	}

	@Test
	@DisplayName("D-Day 기준으로 캘린더를 조회할 수 있다")
	void get_calendar_for_dday() {
		// given
		int dDay = 7;
		CalendarModel calendar = CalendarFixture.캘린더_모델(CALENDAR_ID, MEMBER_ID);
		List<CalendarModel> calendars = List.of(calendar);

		when(calendarRepository.findNotStarted(any(LocalDateTime.class), any(LocalDateTime.class)))
				.thenReturn(calendars);
		when(memberRepository.findNameById(MEMBER_ID)).thenReturn("테스트 작성자");

		// when
		List<CalendarResponse> result = calendarQueryService.getCalendarForDDay(dDay);

		// then
		assertNotNull(result);
		assertEquals(1, result.size());
	}

	@Test
	@DisplayName("캘린더가 없는 경우 빈 리스트를 반환한다")
	void get_calendar_empty_result() {
		// given
		CalendarQuery query = CalendarFixture.캘린더_조회_쿼리_월별();

		when(calendarRepository.findByBetweenDate(any(LocalDateTime.class), any(LocalDateTime.class)))
				.thenReturn(List.of());

		// when
		List<CalendarResponse> result = calendarQueryService.getCalendar(query);

		// then
		assertNotNull(result);
		assertTrue(result.isEmpty());
	}

	@Test
	@DisplayName("여러 캘린더를 조회할 수 있다")
	void get_multiple_calendars() {
		// given
		CalendarQuery query = CalendarFixture.캘린더_조회_쿼리_월별();
		CalendarModel calendar1 = CalendarFixture.캘린더_모델(1L, MEMBER_ID);
		CalendarModel calendar2 = CalendarFixture.캘린더_모델_이벤트(2L, MEMBER_ID);
		CalendarModel calendar3 = CalendarFixture.캘린더_모델_발표(3L, MEMBER_ID);
		List<CalendarModel> calendars = List.of(calendar1, calendar2, calendar3);

		when(calendarRepository.findByBetweenDate(any(LocalDateTime.class), any(LocalDateTime.class)))
				.thenReturn(calendars);
		when(memberRepository.findNameById(MEMBER_ID)).thenReturn("테스트 작성자");

		// when
		List<CalendarResponse> result = calendarQueryService.getCalendar(query);

		// then
		assertNotNull(result);
		assertEquals(3, result.size());
	}
}
