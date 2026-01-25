package com.blackcompany.eeos.notification.application.port;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NotificationResult {
	private boolean success;
	private NotificationErrorCode errorCode;

	public static NotificationResult success(){
		return new NotificationResult(true, null);
	}

	public static NotificationResult fail(NotificationErrorCode errorCode){
		return new NotificationResult(false, errorCode);
	}

}
