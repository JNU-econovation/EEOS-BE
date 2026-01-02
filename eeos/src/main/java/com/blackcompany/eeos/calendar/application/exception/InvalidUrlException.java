package com.blackcompany.eeos.calendar.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class InvalidUrlException extends BusinessException {

	private static final String FAIL_CODE = "10004";

	public InvalidUrlException() {
		super(FAIL_CODE, HttpStatus.BAD_REQUEST);
	}

	@Override
	public String getMessage() {
		return "유효하지 않은 URL 형식입니다.";
	}
}
