package com.blackcompany.eeos.notification.infra.fcm;

import org.springframework.stereotype.Component;

import com.blackcompany.eeos.notification.application.port.NotificationErrorCode;
import com.blackcompany.eeos.notification.application.port.NotificationMessage;
import com.blackcompany.eeos.notification.application.port.NotificationResult;
import com.blackcompany.eeos.notification.application.port.NotificationSender;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FCMNotificationSender implements NotificationSender {

	private final FirebaseMessaging firebaseMessaging;
	private final FCMErrorMapper fcmErrorMapper;

	@Override
	public NotificationResult send(NotificationMessage notificationMessage) {
		Message message = Message.builder()
			.setToken(notificationMessage.getPushToken())
			.setNotification(
				Notification.builder()
					.setTitle(notificationMessage.getMessageTitle())
					.setBody(notificationMessage.getMessageBody())
					.build()
			)
			.build();


		try{
			firebaseMessaging.send(message);
			return NotificationResult.success();
		} catch (FirebaseMessagingException e) {
			NotificationErrorCode errorCode = fcmErrorMapper.map(e);
			return NotificationResult.fail(errorCode);
		}
	}
}
