package com.blackcompany.eeos.auth.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class InvalidGrantException extends BusinessException {
	private static final String FAIL_CODE = "4016";

	public InvalidGrantException() {
		super(FAIL_CODE, HttpStatus.BAD_REQUEST);
	}

	@Override
	public String getMessage() {
		return "유효하지 않은 인가 코드입니다.";
	}
}
