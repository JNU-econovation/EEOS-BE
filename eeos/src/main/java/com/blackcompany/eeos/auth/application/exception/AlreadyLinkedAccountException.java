package com.blackcompany.eeos.auth.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class AlreadyLinkedAccountException extends BusinessException {

	private static final String FAIL_CODE = "4201";

	public AlreadyLinkedAccountException() {
		super(FAIL_CODE, HttpStatus.CONFLICT);
	}

	@Override
	public String getMessage() {
		return "이미 계정이 연결된 회원입니다.";
	}
}
