package com.blackcompany.eeos.notification.persistence;

import com.blackcompany.eeos.notification.application.model.NotificationPermission;
import com.blackcompany.eeos.notification.application.model.NotificationProvider;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaMemberPushTokenRepository extends JpaRepository<NotificationTokenEntity, Long> {

	Optional<NotificationTokenEntity> findByPushToken(String token);

	List<NotificationTokenEntity> findByMemberId(Long memberId);

	List<NotificationTokenEntity> findByMemberIdAndProvider(
			Long memberId, NotificationProvider provider);

	Optional<NotificationTokenEntity> findByMemberIdAndPushToken(Long memberId, String token);

	@Modifying
	@Query("DELETE FROM NotificationTokenEntity t WHERE t.memberId = :memberId")
	void deleteByMemberId(@Param("memberId") Long memberId);

	void deleteByPushToken(String token);

	int deleteByUpdatedDateBefore(Timestamp updatedDate);

	@Modifying
	@Query("DELETE FROM NotificationTokenEntity t where t.lastActiveAt < :limitDate")
	int deleteByLastActiveAtBefore(@Param("limitDate") LocalDateTime limitDate);

	@Query("SELECT t FROM NotificationTokenEntity t WHERE t.memberId IN :memberIds AND t.notificationPermission = :permission")
	List<NotificationTokenEntity> findByMemberIdInAndNotificationPermission(
		@Param("memberIds") List<Long> memberIds,
		@Param("permission") NotificationPermission permission
	);
}
