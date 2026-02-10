package com.blackcompany.eeos.notification.application.exception;

public class RetryableNotificationException extends RuntimeException {

	public RetryableNotificationException() {
		super("일시적 알림 에러. 알림 전송을 재시도 합니다.");
	}
}
