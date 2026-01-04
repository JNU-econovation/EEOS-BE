package com.blackcompany.eeos.notification.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class DeniedDeletePushTokenException extends BusinessException {
	private static final String FAIL_CODE = "5008";

	public DeniedDeletePushTokenException() {
		super(FAIL_CODE, HttpStatus.UNAUTHORIZED);
	}

	@Override
	public String getMessage() {
		return "토큰 수정 권한이 없습니다.";
	}
}
