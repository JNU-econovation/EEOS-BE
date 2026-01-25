package com.blackcompany.eeos.notification.application.service;

import org.springframework.stereotype.Service;

import com.blackcompany.eeos.notification.application.port.NotificationMessage;
import com.blackcompany.eeos.notification.application.port.NotificationSender;
import com.blackcompany.eeos.notification.application.repository.MemberPushTokenRepository;
import com.blackcompany.eeos.notification.application.repository.NotificationLogRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

	private final NotificationSender notificationSender;
	private final MemberPushTokenRepository memberPushTokenRepository;
	private final NotificationLogRepository notificationLogRepository;
	private final SlackNotificationService slackNotificationService;

	public void sendNotification(NotificationMessage notificationMessage) {
		notificationSender.send(notificationMessage);
	}

	public void sendToSingleToken(){

	}

}
