package com.blackcompany.eeos.auth.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class AmbiguousTokenException extends BusinessException {
	private static final String FAIL_CODE = "4013";

	public AmbiguousTokenException() {
		super(FAIL_CODE, HttpStatus.BAD_REQUEST);
	}

	@Override
	public String getMessage() {
		return "토큰 전달 경로가 모호합니다.";
	}
}
