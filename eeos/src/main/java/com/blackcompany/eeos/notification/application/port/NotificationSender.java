package com.blackcompany.eeos.notification.application.port;

public interface NotificationSender {
	NotificationResult send(NotificationMessage notificationMessage);
}
