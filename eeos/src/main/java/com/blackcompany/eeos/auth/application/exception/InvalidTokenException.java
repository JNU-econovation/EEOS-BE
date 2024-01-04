package com.blackcompany.eeos.auth.application.exception;

import org.springframework.http.HttpStatus;

/** 토큰이 유효하지 않을 때 발생하는 예외 */
public class InvalidTokenException extends AuthorizationException {
	private static final String FAIL_CODE = "4003";

	public InvalidTokenException() {
		super(FAIL_CODE, HttpStatus.UNAUTHORIZED);
	}

	@Override
	public String getMessage() {
		return "유효하지 않은 토큰입니다.";
	}
}
