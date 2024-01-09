package com.blackcompany.eeos.program.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

/** 오늘 이전의 날짜로 행사를 생성하려고 할 때 발생하는 예외 */
public class OverDateException extends BusinessException {
	private static final String FAIL_CODE = "1001";

	public OverDateException() {
		super(FAIL_CODE, HttpStatus.BAD_REQUEST);
	}

	@Override
	public String getMessage() {
		return "오늘 이후 날짜에 해당하는 행사만 생성할 수 있습니다.";
	}
}
