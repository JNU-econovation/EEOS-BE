package com.blackcompany.eeos.notification.application.repository;

import com.blackcompany.eeos.notification.application.model.NotificationLogModel;
import com.blackcompany.eeos.notification.application.model.NotificationStatus;

import java.util.List;
import java.util.Optional;

public interface NotificationLogRepository {

	NotificationLogModel save(NotificationLogModel notificationLog);

	Optional<NotificationLogModel> findById(Long id);

	List<NotificationLogModel> findByStatus(NotificationStatus status);

	List<NotificationLogModel> findByProgramIdAndScheduledAtAndStatus(
			Long programId, java.time.LocalDateTime scheduledAt, NotificationStatus status);

	Optional<NotificationLogModel> findByProgramIdAndPushTokenAndScheduledAt(
			Long programId, String pushToken, java.time.LocalDateTime scheduledAt);
}