package com.blackcompany.eeos.notification.application.respository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.blackcompany.eeos.notification.application.model.MemberPushTokenModel;
import com.blackcompany.eeos.notification.application.model.NotificationProvider;

public interface MemberPushTokenRepository {
	Optional<MemberPushTokenModel> findByPushToken(String pushToken);

	List<MemberPushTokenModel> findByMemberId(Long memberId);

	List<MemberPushTokenModel> findByMemberIdAndProvider(Long memberId, NotificationProvider provider);

	void deleteByPushToken(String pushToken);

	int deleteByMemberId(Long memberId);

	int deleteByUpdatedDateBefore(LocalDateTime updatedDate);

	MemberPushTokenModel save(MemberPushTokenModel memberPushToken);

}
