package com.blackcompany.eeos.auth.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class InvalidSignupCodeException extends BusinessException {

	private static final String FAIL_CODE = "4202";

	public InvalidSignupCodeException() {
		super(FAIL_CODE, HttpStatus.BAD_REQUEST);
	}

	@Override
	public String getMessage() {
		return "유효하지 않은 가입 코드입니다.";
	}
}
