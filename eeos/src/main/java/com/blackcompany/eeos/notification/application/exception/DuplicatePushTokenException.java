package com.blackcompany.eeos.notification.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class DuplicatePushTokenException extends BusinessException {

	private static final String FAIL_CODE = "5009";

	public DuplicatePushTokenException() {
		super(FAIL_CODE, HttpStatus.CONFLICT);
	}

	@Override
	public String getMessage() {
		return "이미 등록된 푸시 토큰입니다.";
	}
}
