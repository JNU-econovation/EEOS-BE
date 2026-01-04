package com.blackcompany.eeos.notification.application.model.converter;

import com.blackcompany.eeos.common.support.converter.AbstractEntityConverter;
import com.blackcompany.eeos.notification.application.model.MemberPushTokenModel;
import com.blackcompany.eeos.notification.persistence.MemberPushTokenEntity;
import org.springframework.stereotype.Component;

@Component
public class MemberPushTokenEntityConverter
		implements AbstractEntityConverter<MemberPushTokenEntity, MemberPushTokenModel> {

	@Override
	public MemberPushTokenModel from(MemberPushTokenEntity source) {
		return MemberPushTokenModel.builder()
				.id(source.getId())
				.memberId(source.getMemberId())
				.notificationProvider(source.getProvider())
				.pushToken(source.getPushToken())
				.build();
	}

	@Override
	public MemberPushTokenEntity toEntity(MemberPushTokenModel source) {
		return MemberPushTokenEntity.builder()
				.id(source.getId())
				.memberId(source.getMemberId())
				.pushToken(source.getPushToken())
				.provider(source.getNotificationProvider())
				.build();
	}
}
