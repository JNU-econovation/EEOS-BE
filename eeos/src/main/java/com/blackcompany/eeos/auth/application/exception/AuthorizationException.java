package com.blackcompany.eeos.auth.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

/** 인가와 관련된 문제일 때 발생하는 상위 예외 클래스 */
public class AuthorizationException extends BusinessException {
	public AuthorizationException(String code, HttpStatus httpStatus) {
		super(code, httpStatus);
	}
}
