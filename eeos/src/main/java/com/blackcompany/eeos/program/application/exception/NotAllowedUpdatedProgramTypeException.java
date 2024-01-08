package com.blackcompany.eeos.program.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

/** 프로그램 타입을 수정하려고 할 때 발생하는 예외 */
public class NotAllowedUpdatedProgramTypeException extends BusinessException {
	private static final String FAIL_CODE = "1006";

	public NotAllowedUpdatedProgramTypeException() {
		super(FAIL_CODE, HttpStatus.BAD_REQUEST);
	}

	@Override
	public String getMessage() {
		return "프로그램 수요조사 여부는 수정할 수 없습니다.";
	}
}
