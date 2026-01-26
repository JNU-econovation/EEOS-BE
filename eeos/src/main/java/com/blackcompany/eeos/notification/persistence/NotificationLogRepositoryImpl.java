package com.blackcompany.eeos.notification.persistence;

import com.blackcompany.eeos.notification.application.model.NotificationLogModel;
import com.blackcompany.eeos.notification.application.model.NotificationStatus;
import com.blackcompany.eeos.notification.application.model.converter.NotificationLogEntityConverter;
import com.blackcompany.eeos.notification.application.repository.NotificationLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
		return jpaRepository.findByStatus(status).stream()
				.map(converter::from)
				.toList();
	}

	@Override
	public List<NotificationLogModel> findByProgramIdAndScheduledAtAndStatus(
			Long programId, LocalDateTime scheduledAt, NotificationStatus status) {
		return jpaRepository.findByProgramIdAndScheduledAtAndStatus(programId, scheduledAt, status)
				.stream()
				.map(converter::from)
				.toList();
	}

	@Override
	public Optional<NotificationLogModel> findByProgramIdAndPushTokenAndScheduledAt(
			Long programId, String pushToken, LocalDateTime scheduledAt) {
		return jpaRepository.findByProgramIdAndPushTokenAndScheduledAt(programId, pushToken, scheduledAt)
				.map(converter::from);
	}
}