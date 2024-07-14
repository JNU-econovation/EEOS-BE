package com.blackcompany.eeos.program.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class AlreadyEndProgramException extends BusinessException {

	private static final String FAIL_CODE = "1010";

	public AlreadyEndProgramException() {
		super(FAIL_CODE, HttpStatus.UNAUTHORIZED);
	}

	@Override
	public String getMessage() {
		return "종료된 행사는 출석 체크를 시작할 수 없습니다.";
	}
}
