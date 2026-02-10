package com.blackcompany.eeos.notification.application.scheduler;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.blackcompany.eeos.calendar.application.dto.CalendarResponse;
import com.blackcompany.eeos.calendar.application.model.CalendarType;
import com.blackcompany.eeos.calendar.application.service.CalendarQueryService;
import com.blackcompany.eeos.notification.application.dto.NotificationRequest;
import com.blackcompany.eeos.notification.application.service.NotificationService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CalendarNotificationSchedulerTest {

	@Mock private CalendarQueryService calendarQueryService;
	@Mock private NotificationService notificationService;

	private CalendarNotificationScheduler scheduler;

	@BeforeEach
	void setUp() {
		scheduler = new CalendarNotificationScheduler(calendarQueryService, notificationService);
	}

	@Test
	@DisplayName("아침 알림은 당일 일정에 대해 알림을 보낸다")
	void morning_notification_sends_for_today_events() {
		// given
		CalendarResponse todayEvent =
				new CalendarResponse(1L, "정기회의", "http://url", "event", 0L, 0L, "writer");
		when(calendarQueryService.getCalendarByStartAt(any())).thenReturn(List.of(todayEvent));

		// when
		scheduler.sendMorningNotification();

		// then
		ArgumentCaptor<NotificationRequest> captor = ArgumentCaptor.forClass(NotificationRequest.class);
		verify(notificationService, times(1)).sendNotification(captor.capture());

		NotificationRequest request = captor.getValue();

		assertTrue(request.getBody().contains("당일입니다"));
		assertEquals("EEOS 일정 알림", request.getTitle());
	}

	@Test
	@DisplayName("당일 일정이 없으면 알림을 보내지 않는다")
	void morning_notification_no_events() {
		// given
		when(calendarQueryService.getCalendarByStartAt(any())).thenReturn(List.of());

		// when
		scheduler.sendMorningNotification();

		// then
		verify(notificationService, never()).sendNotification(any());
	}

	@Test
	@DisplayName("알림 본문에 일정 제목과 남은 일수가 포함된다")
	void notification_body_contains_title_and_days() {
		// given
		CalendarResponse event =
				new CalendarResponse(1L, "정기회의", "http://url", "event", 0L, 0L, "writer");
		when(calendarQueryService.getCalendarByStartAt(any())).thenReturn(List.of(event));

		// when
		scheduler.sendMorningNotification();

		// then
		ArgumentCaptor<NotificationRequest> captor = ArgumentCaptor.forClass(NotificationRequest.class);
		verify(notificationService).sendNotification(captor.capture());

		NotificationRequest request = captor.getValue();
		assertEquals("정기회의 - 당일입니다.", request.getBody());
	}

	@Test
	@DisplayName("PRESENTATION 타입은 1일 전에만 알림 대상이 된다")
	void presentation_type_only_1_day_before() {
		// given
		List<Integer> days = CalendarType.PRESENTATION.getNotificationDays();

		// then
		assertAll("days 검증",
			() -> assertEquals(1, days.size()),
			() -> assertTrue(days.contains(1)),
			() -> assertFalse(days.contains(3)),
			() -> assertFalse(days.contains(5))
		);
	}

	@Test
	@DisplayName("ETC 타입은 3일, 1일 전에 알림 대상이 된다")
	void etc_type_3_and_1_day_before() {
		// given
		List<Integer> days = CalendarType.ETC.getNotificationDays();

		// then
		assertAll("Days 리스트 검증",
			() -> assertEquals(2, days.size(), "사이즈는 2여야 합니다."),
			() -> assertTrue(days.contains(3), "3을 포함해야 합니다."),
			() -> assertTrue(days.contains(1), "1을 포함해야 합니다."),
			() -> assertFalse(days.contains(5), "5를 포함하지 않아야 합니다.")
		);
	}
}
