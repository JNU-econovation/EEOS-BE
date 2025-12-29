package com.blackcompany.eeos.notification.application.model;

import java.time.LocalDateTime;

import com.blackcompany.eeos.common.support.AbstractModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberPushTokenModel implements AbstractModel {
	private Long id;
	private Long memberId;
	private NotificationProvider notificationProvider;
	private String pushToken;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
