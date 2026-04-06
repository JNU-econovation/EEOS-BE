package com.blackcompany.eeos.auth.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class InvalidClientException extends BusinessException {
	private static final String FAIL_CODE = "4014";

	public InvalidClientException() {
		super(FAIL_CODE, HttpStatus.BAD_REQUEST);
	}

	@Override
	public String getMessage() {
		return "유효하지 않은 클라이언트입니다.";
	}
}
