package com.blackcompany.eeos.notification.application.dto;

import com.blackcompany.eeos.calendar.application.model.CalendarType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class NotificationRequest {
	private LocalDateTime scheduledAt;
	private Long calendarId;
	private CalendarType calendarType;
	private String title;
	private String body;
}
