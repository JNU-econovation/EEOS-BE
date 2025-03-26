package com.blackcompany.eeos.common.exception;

import org.springframework.http.HttpStatus;

public class InvalidParameterException extends BusinessException {

	private static final String errorCode = "9000";

	public InvalidParameterException() {
		super(errorCode, HttpStatus.BAD_REQUEST);
	}

	@Override
	public String getMessage() {
		return "요청 파라미터가 잘못되었습니다.";
	}
}
