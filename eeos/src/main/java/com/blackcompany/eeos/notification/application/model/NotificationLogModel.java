package com.blackcompany.eeos.notification.application.model;

import com.blackcompany.eeos.calendar.application.model.CalendarType;
import com.blackcompany.eeos.common.support.AbstractModel;
import com.blackcompany.eeos.notification.application.port.NotificationErrorCode;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class NotificationLogModel implements AbstractModel {

	private Long id;
	private Long calendarId;
	private String pushToken;
	private CalendarType calendarType;
	private String messageTitle;
	private String messageBody;
	private NotificationStatus status;
	private NotificationErrorCode errorCode;
	private LocalDateTime scheduledAt;
	private LocalDateTime sentAt;
	private NotificationProvider provider;
}
