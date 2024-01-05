package com.blackcompany.eeos.program.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

/** 존재하지 않는 프로그램 타입일 때 발생하는 예외 */
public class NotFoundProgramTypeException extends BusinessException {
	private static final String FAIL_CODE = "1003";
	private final String type;

	public NotFoundProgramTypeException(String type) {
		super(FAIL_CODE, HttpStatus.NOT_FOUND);
		this.type = type;
	}

	@Override
	public String getMessage() {
		return String.format("%s 는 존재하지 않는 프로그램 타입입니다.", type);
	}
}
