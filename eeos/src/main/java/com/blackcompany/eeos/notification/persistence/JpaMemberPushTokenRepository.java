package com.blackcompany.eeos.notification.persistence;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.blackcompany.eeos.notification.application.model.NotificationProvider;

public interface JpaMemberPushTokenRepository extends JpaRepository<MemberPushTokenEntity, Long> {

	Optional<MemberPushTokenEntity> findByPushToken(String token);

	List<MemberPushTokenEntity> findByMemberId(Long memberId);

	List<MemberPushTokenEntity> findByMemberIdAndProvider(Long memberId, NotificationProvider provider);

	@Modifying
	@Query("DELETE FROM MemberPushTokenEntity t WHERE t.memberId = :memberId")
	int deleteByMemberId(@Param("memberId") Long memberId);

	void deleteByPushToken(String token);

	int deleteByUpdatedDateBefore(Timestamp updatedDate);
}
