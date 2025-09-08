package com.blackcompany.eeos.calendar.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class DeniedCalendarUpdateException extends BusinessException {

	private static final String FAIL_CODE = "10002";

	public DeniedCalendarUpdateException() {
		super(FAIL_CODE, HttpStatus.BAD_REQUEST);
	}

	@Override
	public String getMessage() {
		return "칼렌더를 수정할 권한이 없습니다.";
	}
}
