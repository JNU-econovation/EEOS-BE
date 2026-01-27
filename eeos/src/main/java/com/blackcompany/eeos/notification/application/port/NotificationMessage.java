package com.blackcompany.eeos.notification.application.port;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class NotificationMessage {
	private String pushToken;
	private String messageTitle;
	private String messageBody;
}
