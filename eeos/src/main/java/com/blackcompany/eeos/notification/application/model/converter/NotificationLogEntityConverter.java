package com.blackcompany.eeos.notification.application.model.converter;

import com.blackcompany.eeos.common.support.converter.AbstractEntityConverter;
import com.blackcompany.eeos.notification.application.model.NotificationLogModel;
import com.blackcompany.eeos.notification.persistence.NotificationLogEntity;
import org.springframework.stereotype.Component;

@Component
public class NotificationLogEntityConverter
		implements AbstractEntityConverter<NotificationLogEntity, NotificationLogModel> {

	@Override
	public NotificationLogModel from(NotificationLogEntity source) {
		return NotificationLogModel.builder()
				.id(source.getId())
				.calendarId(source.getCalendarId())
				.pushToken(source.getPushToken())
				.calendarType(source.getCalendarType())
				.messageTitle(source.getMessageTitle())
				.messageBody(source.getMessageBody())
				.status(source.getStatus())
				.errorCode(source.getErrorCode())
				.scheduledAt(source.getScheduledAt())
				.sentAt(source.getSentAt())
				.provider(source.getProvider())
				.build();
	}

	@Override
	public NotificationLogEntity toEntity(NotificationLogModel source) {
		return NotificationLogEntity.builder()
				.id(source.getId())
				.calendarId(source.getCalendarId())
				.pushToken(source.getPushToken())
				.calendarType(source.getCalendarType())
				.messageTitle(source.getMessageTitle())
				.messageBody(source.getMessageBody())
				.status(source.getStatus())
				.errorCode(source.getErrorCode())
				.scheduledAt(source.getScheduledAt())
				.sentAt(source.getSentAt())
				.provider(source.getProvider())
				.build();
	}
}
