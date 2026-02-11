package com.blackcompany.eeos.notification.persistence;

import com.blackcompany.eeos.notification.application.model.NotificationStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaNotificationLogRepository extends JpaRepository<NotificationLogEntity, Long> {

	List<NotificationLogEntity> findByStatus(NotificationStatus status);

	List<NotificationLogEntity> findByCalendarIdAndScheduledAtAndStatus(
			Long calendarId, LocalDateTime scheduledAt, NotificationStatus status);

	Optional<NotificationLogEntity> findByCalendarIdAndPushTokenAndScheduledAt(
			Long calendarId, String pushToken, LocalDateTime scheduledAt);
}
