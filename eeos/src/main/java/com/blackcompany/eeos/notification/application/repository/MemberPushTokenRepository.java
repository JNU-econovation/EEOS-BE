package com.blackcompany.eeos.notification.application.repository;

import com.blackcompany.eeos.notification.application.model.MemberPushTokenModel;
import com.blackcompany.eeos.notification.application.model.NotificationProvider;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MemberPushTokenRepository {
	Optional<MemberPushTokenModel> findByPushToken(String pushToken);

	List<MemberPushTokenModel> findByMemberId(Long memberId);

	List<MemberPushTokenModel> findByMemberIdAndProvider(
			Long memberId, NotificationProvider provider);

	Optional<MemberPushTokenModel> findByMemberIdAndPushToken(Long memberId, String pushToken);

	void deleteByPushToken(String pushToken);

	void deleteByMemberId(Long memberId);

	int deleteByUpdatedDateBefore(LocalDateTime updatedDate);

	MemberPushTokenModel save(MemberPushTokenModel memberPushToken);
}
