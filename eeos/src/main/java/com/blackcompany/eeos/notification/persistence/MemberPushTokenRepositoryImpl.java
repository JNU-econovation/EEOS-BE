package com.blackcompany.eeos.notification.persistence;

import com.blackcompany.eeos.notification.application.model.MemberPushTokenModel;
import com.blackcompany.eeos.notification.application.model.NotificationPermission;
import com.blackcompany.eeos.notification.application.model.NotificationProvider;
import com.blackcompany.eeos.notification.application.model.converter.MemberPushTokenEntityConverter;
import com.blackcompany.eeos.notification.application.repository.MemberPushTokenRepository;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberPushTokenRepositoryImpl implements MemberPushTokenRepository {

	private final JpaMemberPushTokenRepository jpaRepository;

	private final MemberPushTokenEntityConverter converter;

	@Override
	public Optional<MemberPushTokenModel> findByPushToken(String pushToken) {
		return jpaRepository.findByPushToken(pushToken).map(converter::from);
	}

	@Override
	public List<MemberPushTokenModel> findByMemberId(Long memberId) {
		return jpaRepository.findByMemberId(memberId).stream().map(converter::from).toList();
	}

	@Override
	public Optional<MemberPushTokenModel> findByMemberIdAndPushToken(
			Long memberId, String pushToken) {
		return jpaRepository.findByMemberIdAndPushToken(memberId, pushToken).map(converter::from);
	}

	@Override
	public List<MemberPushTokenModel> findByMemberIdAndProvider(
			Long memberId, NotificationProvider provider) {
		return jpaRepository.findByMemberIdAndProvider(memberId, provider).stream()
				.map(converter::from)
				.toList();
	}

	@Override
	public void deleteByPushToken(String pushToken) {
		jpaRepository.deleteByPushToken(pushToken);
	}

	@Override
	public void deleteByMemberId(Long memberId) {
		jpaRepository.deleteByMemberId(memberId);
	}

	@Override
	public int deleteByUpdatedDateBefore(LocalDateTime updatedDate) {
		Timestamp timestamp = Timestamp.valueOf(updatedDate);
		return jpaRepository.deleteByUpdatedDateBefore(timestamp);
	}

	@Override
	public MemberPushTokenModel save(MemberPushTokenModel memberPushToken) {
		NotificationTokenEntity notificationTokenEntity = converter.toEntity(memberPushToken);
		NotificationTokenEntity saved = jpaRepository.save(notificationTokenEntity);
		return converter.from(saved);
	}

	@Override
	public MemberPushTokenModel saveAndFlush(MemberPushTokenModel memberPushToken) {
		NotificationTokenEntity notificationTokenEntity = converter.toEntity(memberPushToken);
		NotificationTokenEntity saved = jpaRepository.saveAndFlush(notificationTokenEntity);
		return converter.from(saved);
	}

	@Override
	public int deleteByLastActiveAtBefore(LocalDateTime limitDate) {
		return jpaRepository.deleteByLastActiveAtBefore(limitDate);
	}

	@Override
	public List<MemberPushTokenModel> findByMemberIdsAndNotificationPermission(
			List<Long> memberIds, NotificationPermission permission) {
		if (memberIds.isEmpty()) {
			return List.of();
		}
		return jpaRepository.findByMemberIdInAndNotificationPermission(memberIds, permission).stream()
				.map(converter::from)
				.toList();
	}

	@Override
	public void deleteByPushTokenIn(List<String> pushTokens) {
		jpaRepository.deleteByPushTokenIn(pushTokens);
	}
}
