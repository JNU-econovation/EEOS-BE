package com.blackcompany.eeos.program.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class NotAllowedAttendStartException extends BusinessException {

	private static final String FAIL_CODE = "1011";

	public NotAllowedAttendStartException() {
		super(FAIL_CODE, HttpStatus.NOT_ACCEPTABLE);
	}

	@Override
	public String getMessage() {
		return "관리자만 출석체크를 시작할 수 있습니다.";
	}
}
