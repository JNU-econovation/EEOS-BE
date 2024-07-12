package com.blackcompany.eeos.program.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class SameModeRequestException extends BusinessException {

	private static final String FAIL_CODE = "1012";

	public SameModeRequestException() {
		super(FAIL_CODE, HttpStatus.NOT_ACCEPTABLE);
	}

	@Override
	public String getMessage() {
		return "현재 출석 체크 진행 상태와 동일한 모드로 요청하였습니다.";
	}
}
