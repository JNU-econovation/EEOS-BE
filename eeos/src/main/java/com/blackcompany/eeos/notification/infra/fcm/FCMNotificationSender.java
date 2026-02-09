package com.blackcompany.eeos.notification.infra.fcm;

import com.blackcompany.eeos.notification.application.model.NotificationProvider;
import com.blackcompany.eeos.notification.application.port.NotificationErrorCode;
import com.blackcompany.eeos.notification.application.port.NotificationMessage;
import com.blackcompany.eeos.notification.application.port.NotificationResult;
import com.blackcompany.eeos.notification.application.port.NotificationSender;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.SendResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FCMNotificationSender implements NotificationSender {

	private final FirebaseMessaging firebaseMessaging;
	private final FCMErrorMapper fcmErrorMapper;

	@Override
	public NotificationResult send(NotificationMessage notificationMessage) {
		Message message =
				Message.builder()
						.setToken(notificationMessage.getPushToken())
						.setNotification(
								Notification.builder()
										.setTitle(notificationMessage.getMessageTitle())
										.setBody(notificationMessage.getMessageBody())
										.build())
						.build();

		try {
			firebaseMessaging.send(message);
			return NotificationResult.success();
		} catch (FirebaseMessagingException e) {
			NotificationErrorCode errorCode = fcmErrorMapper.map(e);
			return NotificationResult.fail(errorCode);
		}
	}

	@Override
	public Map<String, NotificationResult> sendAll(String title, String body, List<String> tokens) {
		// 메세지 생성
		MulticastMessage message =
				MulticastMessage.builder()
						.setNotification(Notification.builder().setTitle(title).setBody(body).build())
						.addAllTokens(tokens)
						.build();

		Map<String, NotificationResult> results = new HashMap<>();
		// 메세지 전송
		try {
			BatchResponse response = firebaseMessaging.sendEachForMulticast(message);

			if (response.getFailureCount() == 0) {
				for (String token : tokens) {
					results.put(token, NotificationResult.success());
				}
				return results;
			}

			List<SendResponse> sendResponses = response.getResponses();
			for (int i = 0; i < sendResponses.size(); i++) {
				String token = tokens.get(i);
				SendResponse sendResponse = sendResponses.get(i);

				if (sendResponse.isSuccessful()) {
					results.put(token, NotificationResult.success());
				} else {
					NotificationErrorCode errorCode = fcmErrorMapper.map(sendResponse.getException());
					results.put(token, NotificationResult.fail(errorCode));
				}
			}
		} catch (FirebaseMessagingException e) {
			for (String token : tokens) {
				results.put(token, NotificationResult.fail(NotificationErrorCode.TEMPORARY_ERROR));
			}
		}

		return results;
	}

	@Override
	public NotificationProvider getNotificationProvider() {
		return NotificationProvider.FCM;
	}
}
