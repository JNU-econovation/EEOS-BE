package com.blackcompany.eeos.notification.infra.fcm;

import com.blackcompany.eeos.notification.application.port.NotificationErrorCode;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MessagingErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FCMErrorMapper {
	public NotificationErrorCode map(FirebaseMessagingException e) {

		MessagingErrorCode errorCode = e.getMessagingErrorCode();

		log.warn(
				"FCM 발송 실패 - messagingErrorCode={}, errorCode={}, httpStatus={}, message={}",
				errorCode,
				e.getErrorCode(),
				e.getHttpResponse() != null ? e.getHttpResponse().getStatusCode() : "N/A",
				e.getMessage());

		if (errorCode == null) {
			return NotificationErrorCode.UNKNOWN_ERROR;
		}
		return switch (errorCode) {
			case UNREGISTERED, INVALID_ARGUMENT, SENDER_ID_MISMATCH -> NotificationErrorCode
					.INVALID_TOKEN;
			case UNAVAILABLE, INTERNAL, QUOTA_EXCEEDED -> NotificationErrorCode.TEMPORARY_ERROR;
			default -> NotificationErrorCode.UNKNOWN_ERROR;
		};
	}
}
