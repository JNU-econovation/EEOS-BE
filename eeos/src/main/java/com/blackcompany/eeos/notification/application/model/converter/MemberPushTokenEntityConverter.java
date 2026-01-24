package com.blackcompany.eeos.notification.application.model.converter;

import com.blackcompany.eeos.common.support.converter.AbstractEntityConverter;
import com.blackcompany.eeos.notification.application.model.MemberPushTokenModel;
import com.blackcompany.eeos.notification.persistence.NotificationTokenEntity;
import org.springframework.stereotype.Component;

@Component
public class MemberPushTokenEntityConverter
		implements AbstractEntityConverter<NotificationTokenEntity, MemberPushTokenModel> {

	@Override
	public MemberPushTokenModel from(NotificationTokenEntity source) {
		return MemberPushTokenModel.builder()
				.id(source.getId())
				.memberId(source.getMemberId())
				.notificationProvider(source.getProvider())
				.pushToken(source.getPushToken())
				.lastActiveAt(source.getLastActiveAt())
				.pushStatus(source.getPushStatus())
				.build();
	}

	@Override
	public NotificationTokenEntity toEntity(MemberPushTokenModel source) {
		return NotificationTokenEntity.builder()
				.id(source.getId())
				.memberId(source.getMemberId())
				.pushToken(source.getPushToken())
				.provider(source.getNotificationProvider())
				.lastActiveAt(source.getLastActiveAt())
				.pushStatus(source.getPushStatus())
				.build();
	}
}
