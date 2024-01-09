package com.blackcompany.eeos.auth.application.exception;

import org.springframework.http.HttpStatus;

/** RT 토큰이 만료되었을 때 발생하는 예외 */
public class RtTokenExpiredException extends AuthorizationException {
	private static final String FAIL_CODE = "4005";

	public RtTokenExpiredException() {
		super(FAIL_CODE, HttpStatus.FORBIDDEN);
	}

	@Override
	public String getMessage() {
		return "리프레시 토큰이 만료되었습니다.";
	}
}
