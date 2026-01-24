package com.blackcompany.eeos.notification.application.model;

import com.blackcompany.eeos.common.support.AbstractModel;
import com.blackcompany.eeos.notification.application.exception.DeniedUpdatePushTokenException;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class MemberPushTokenModel implements AbstractModel {
	private Long id;
	private Long memberId;
	private NotificationProvider notificationProvider;
	private String pushToken;
	private LocalDateTime lastActiveAt;
	private NotificationPermission notificationPermission;

	public MemberPushTokenModel renew(Long memberId) {
		return this.toBuilder().memberId(memberId).lastActiveAt(LocalDateTime.now()).notificationPermission(
			NotificationPermission.ON).build();
	}

	public void validateTokenOwner(Long requestMemberId) {
		if(!this.memberId.equals(requestMemberId)) {
			throw new DeniedUpdatePushTokenException();
		}
	}

	public MemberPushTokenModel updateNotificationPermission(NotificationPermission notificationPermission) {
		return this.toBuilder().notificationPermission(notificationPermission).lastActiveAt(LocalDateTime.now()).build();
	}
}
