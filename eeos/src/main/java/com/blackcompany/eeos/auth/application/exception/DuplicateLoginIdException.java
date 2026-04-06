package com.blackcompany.eeos.auth.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class DuplicateLoginIdException extends BusinessException {

	private static final String FAIL_CODE = "4009";

	public DuplicateLoginIdException() {
		super(FAIL_CODE, HttpStatus.CONFLICT);
	}

	@Override
	public String getMessage() {
		return "이미 사용 중인 아이디입니다.";
	}
}
