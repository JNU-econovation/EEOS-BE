package com.blackcompany.eeos.notification.persistence;

import com.blackcompany.eeos.notification.application.model.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface JpaNotificationLogRepository extends JpaRepository<NotificationLogEntity, Long> {

	List<NotificationLogEntity> findByStatus(NotificationStatus status);

	List<NotificationLogEntity> findByProgramIdAndScheduledAtAndStatus(
			Long programId, LocalDateTime scheduledAt, NotificationStatus status);

	Optional<NotificationLogEntity> findByProgramIdAndPushTokenAndScheduledAt(
			Long programId, String pushToken, LocalDateTime scheduledAt);
}