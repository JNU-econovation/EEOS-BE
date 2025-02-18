package com.blackcompany.eeos.auth.persistence.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class ExpiredVerificationException extends BusinessException {
	private static final String FAIL_CODE = "4009";

	public ExpiredVerificationException() {
		super(FAIL_CODE, HttpStatus.UNAUTHORIZED);
	}

	@Override
	public String getMessage() {
		return "임시 인증 정보가 만료되었습니다.";
	}
}
