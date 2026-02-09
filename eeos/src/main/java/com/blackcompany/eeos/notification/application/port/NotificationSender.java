package com.blackcompany.eeos.notification.application.port;

import com.blackcompany.eeos.notification.application.model.NotificationProvider;
import java.util.List;
import java.util.Map;

public interface NotificationSender {
	NotificationResult send(NotificationMessage notificationMessage);

	Map<String, NotificationResult> sendAll(String title, String body, List<String> tokens);

	NotificationProvider getNotificationProvider();
}
