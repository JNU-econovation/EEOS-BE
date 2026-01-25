package com.blackcompany.eeos.notification.application.model.converter;

import org.springframework.stereotype.Component;

import com.blackcompany.eeos.common.support.converter.AbstractEntityConverter;
import com.blackcompany.eeos.notification.application.model.NotificationLogModel;
import com.blackcompany.eeos.notification.persistence.NotificationLogEntity;

@Component
public class NotificationLogEntityConverter implements AbstractEntityConverter<NotificationLogEntity, NotificationLogModel> {

	@Override
	public NotificationLogModel from(NotificationLogEntity source) {
		return NotificationLogModel.builder()
			.id(source.getId())
			.programId(source.getProgramId())
			.pushToken(source.getPushToken())
			.programCategory(source.getProgramCategory())
			.messageTitle(source.getMessageTitle())
			.messageBody(source.getMessageBody())
			.status(source.getStatus())
			.errorCode(source.getErrorCode())
			.retryCount(source.getRetryCount())
			.scheduledAt(source.getScheduledAt())
			.sentAt(source.getSentAt())
			.provider(source.getProvider())
			.build();
	}

	@Override
	public NotificationLogEntity toEntity(NotificationLogModel source) {
		return NotificationLogEntity.builder()
			.id(source.getId())
			.programId(source.getProgramId())
			.pushToken(source.getPushToken())
			.programCategory(source.getProgramCategory())
			.messageTitle(source.getMessageTitle())
			.messageBody(source.getMessageBody())
			.status(source.getStatus())
			.errorCode(source.getErrorCode())
			.retryCount(source.getRetryCount())
			.scheduledAt(source.getScheduledAt())
			.sentAt(source.getSentAt())
			.provider(source.getProvider())
			.build();
	}
}
