package com.blackcompany.eeos.calendar.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class InvalidDateException extends BusinessException {

	private static final String FAIL_CODE = "10003";

	public InvalidDateException() {
		super(FAIL_CODE, HttpStatus.BAD_REQUEST);
	}

	@Override
	public String getMessage() {
		return "행사 시작 날짜는 행사 종료 날짜보다 이후일 수 없습니다.";
	}
}
