package com.blackcompany.eeos.auth.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class RateLimitExceededException extends BusinessException {
	private static final String FAIL_CODE = "4290";

	public RateLimitExceededException() {
		super(FAIL_CODE, HttpStatus.TOO_MANY_REQUESTS);
	}

	@Override
	public String getMessage() {
		return "로그인 시도 횟수를 초과했습니다.";
	}
}
