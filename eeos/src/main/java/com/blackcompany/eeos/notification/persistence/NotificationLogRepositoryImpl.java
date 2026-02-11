package com.blackcompany.eeos.notification.persistence;

import com.blackcompany.eeos.notification.application.model.NotificationLogModel;
import com.blackcompany.eeos.notification.application.model.NotificationStatus;
import com.blackcompany.eeos.notification.application.model.converter.NotificationLogEntityConverter;
import com.blackcompany.eeos.notification.application.repository.NotificationLogRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NotificationLogRepositoryImpl implements NotificationLogRepository {

	private final JpaNotificationLogRepository jpaRepository;
	private final NotificationLogEntityConverter converter;

	@Override
	public NotificationLogModel save(NotificationLogModel notificationLog) {
		NotificationLogEntity entity = converter.toEntity(notificationLog);
		NotificationLogEntity saved = jpaRepository.save(entity);
		return converter.from(saved);
	}

	@Override
	public Optional<NotificationLogModel> findById(Long id) {
		return jpaRepository.findById(id).map(converter::from);
	}

	@Override
	public List<NotificationLogModel> findByStatus(NotificationStatus status) {
		return jpaRepository.findByStatus(status).stream().map(converter::from).toList();
	}

	@Override
	public List<NotificationLogModel> findByCalendarIdAndScheduledAtAndStatus(
			Long calendarId, LocalDateTime scheduledAt, NotificationStatus status) {
		return jpaRepository
				.findByCalendarIdAndScheduledAtAndStatus(calendarId, scheduledAt, status)
				.stream()
				.map(converter::from)
				.toList();
	}

	@Override
	public Optional<NotificationLogModel> findByCalendarIdAndPushTokenAndScheduledAt(
			Long calendarId, String pushToken, LocalDateTime scheduledAt) {
		return jpaRepository
				.findByCalendarIdAndPushTokenAndScheduledAt(calendarId, pushToken, scheduledAt)
				.map(converter::from);
	}
}
