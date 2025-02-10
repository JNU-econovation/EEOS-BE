package com.blackcompany.eeos.auth.application.exception;

import org.springframework.http.HttpStatus;

public class InvalidTemporaryAuthenticationException extends AuthorizationException {
	private static final String FAIL_CODE = "4011";

	public InvalidTemporaryAuthenticationException() {
		super(FAIL_CODE, HttpStatus.UNAUTHORIZED);
	}

	@Override
	public String getMessage() {
		return "임시 인증 정보를 찾을 수 없습니다.";
	}
}
