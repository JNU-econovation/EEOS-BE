package com.blackcompany.eeos.notification.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class NotFoundPushTokenException extends BusinessException {

	private static final String FAIL_CODE = "5007";
	private final String pushToken;

	public NotFoundPushTokenException(String pushToken) {

		super(FAIL_CODE, HttpStatus.NOT_FOUND);
		this.pushToken = pushToken;
	}

	@Override
	public String getMessage() {
		return String.format("%s 는 존재하지 않는 알림토큰 입니다.", pushToken);
	}
}
