package com.blackcompany.eeos.auth.application.exception;

import org.springframework.http.HttpStatus;

/** 토큰이 만료되었을 때 발생하는 예외 */
public class TokenExpiredException extends AuthorizationException {
	private static final String MESSAGE = "토큰이 만료되었습니다.";

	public TokenExpiredException() {
		super(MESSAGE, HttpStatus.FORBIDDEN);
	}
}
