package com.blackcompany.eeos.notification.application.scheduler;

import com.blackcompany.eeos.calendar.application.dto.CalendarResponse;
import com.blackcompany.eeos.calendar.application.model.CalendarType;
import com.blackcompany.eeos.calendar.application.service.CalendarQueryService;
import com.blackcompany.eeos.notification.application.dto.NotificationRequest;
import com.blackcompany.eeos.notification.application.service.NotificationService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CalendarNotificationScheduler {

	private final CalendarQueryService calendarQueryService;
	private final NotificationService notificationService;
	private static final String NOTIFICATION_HEAD_TITLE = "EEOS 일정 알림";

	@Scheduled(cron = "${notification.morningTime}")
	public void sendMorningNotification() {
		LocalDateTime today = getStartOfDay(0);
		List<CalendarResponse> todayEvents = calendarQueryService.getCalendarByStartAt(today);

		todayEvents.forEach(event -> sendNotification(event, 0));
	}

	@Scheduled(cron = "${notification.eveningTime}")
	public void sendEveningNotification() {
		for (CalendarType type : CalendarType.values()) {
			for (int day : type.getNotificationDays()) {
				LocalDateTime targetDate = getStartOfDay(day);
				List<CalendarResponse> calendarResponses =
						calendarQueryService.getCalendarByStartAt(targetDate);

				calendarResponses.stream()
						.filter(c -> CalendarType.findByName(c.type()) == type)
						.forEach(c -> sendNotification(c, day));
			}
		}
	}

	private void sendNotification(CalendarResponse calendarResponse, int daysBeforeEvent) {
		String body = createNotificationBody(calendarResponse.title(), daysBeforeEvent);

		NotificationRequest request =
				NotificationRequest.builder()
						.calendarId(calendarResponse.calendarId())
						.calendarType(CalendarType.findByName(calendarResponse.type()))
						.scheduledAt(LocalDateTime.now())
						.title(NOTIFICATION_HEAD_TITLE)
						.body(body)
						.build();

		notificationService.sendNotification(request);
	}

	private String createNotificationBody(String title, int daysBefore) {
		if (daysBefore == 0) {
			return String.format("%s - 당일입니다.", title);
		}
		return String.format("%s - %d일 전입니다.", title, daysBefore);
	}

	private LocalDateTime getStartOfDay(int plusDays) {
		return LocalDateTime.now().plusDays(plusDays).toLocalDate().atStartOfDay();
	}
}
