package com.blackcompany.eeos.target.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

/** 해당 프로그램의 참석 대상자가 아닌데 참석 정보를 입력하려고 할 때 발생하는 예외 */
public class DeniedSaveAttendException extends BusinessException {
	private static final String FAIL_CODE = "2005";

	public DeniedSaveAttendException() {
		super(FAIL_CODE, HttpStatus.NOT_FOUND);
	}

	@Override
	public String getMessage() {
		return String.format(" 프로그램의 참석 대상자가 아닙니다.");
	}
}
