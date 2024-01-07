package com.blackcompany.eeos.auth.application.exception;

import org.springframework.http.HttpStatus;

/** 토큰이 만료되었을 때 발생하는 예외 */
public class TokenExpiredException extends AuthorizationException {
	private static final String FAIL_CODE = "4001";

	public TokenExpiredException() {
		super(FAIL_CODE, HttpStatus.FORBIDDEN);
	}

	@Override
	public String getMessage() {
		return "엑세스 토큰이 만료되었습니다.";
	}
}
