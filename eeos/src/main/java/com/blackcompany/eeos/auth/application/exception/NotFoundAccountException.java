package com.blackcompany.eeos.auth.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class NotFoundAccountException extends BusinessException {

	private static final String FAIL_CODE = "4008";

	public NotFoundAccountException() {
		super(FAIL_CODE, HttpStatus.NOT_FOUND);
	}

	@Override
	public String getMessage() {
		return "ID 또는 비밀번호가 일치하지 않습니다.";
	}
}
