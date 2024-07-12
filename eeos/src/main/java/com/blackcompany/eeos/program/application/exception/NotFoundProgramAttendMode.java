package com.blackcompany.eeos.program.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class NotFoundProgramAttendMode extends BusinessException {

	private static final String FAIL_CODE = "1014";

	public NotFoundProgramAttendMode() {
		super(FAIL_CODE, HttpStatus.NOT_FOUND);
	}

	@Override
	public String getMessage() {
		return "해당 출석체크 모드를 찾을 수 없습니다.";
	}
}
