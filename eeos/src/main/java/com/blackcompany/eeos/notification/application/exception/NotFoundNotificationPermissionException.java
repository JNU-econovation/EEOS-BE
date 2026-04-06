package com.blackcompany.eeos.notification.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class NotFoundNotificationPermissionException extends BusinessException {

	private static final String FAIL_CODE = "5010";
	private final String notificationPermission;

	public NotFoundNotificationPermissionException(String notificationPermission) {

		super(FAIL_CODE, HttpStatus.NOT_FOUND);
		this.notificationPermission = notificationPermission;
	}

	@Override
	public String getMessage() {
		return String.format("%s 는 존재하지 않는 알림상태 입니다.", notificationPermission);
	}
}
