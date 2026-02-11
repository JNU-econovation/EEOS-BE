package com.blackcompany.eeos.notification.application.repository;

import com.blackcompany.eeos.notification.application.model.NotificationLogModel;
import com.blackcompany.eeos.notification.application.model.NotificationStatus;
import java.util.List;
import java.util.Optional;

public interface NotificationLogRepository {

	NotificationLogModel save(NotificationLogModel notificationLog);

	Optional<NotificationLogModel> findById(Long id);

	List<NotificationLogModel> findByStatus(NotificationStatus status);

	List<NotificationLogModel> findByCalendarIdAndScheduledAtAndStatus(
			Long calendarId, java.time.LocalDateTime scheduledAt, NotificationStatus status);

	Optional<NotificationLogModel> findByCalendarIdAndPushTokenAndScheduledAt(
			Long calendarId, String pushToken, java.time.LocalDateTime scheduledAt);
}
