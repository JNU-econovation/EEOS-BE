package com.blackcompany.eeos.program.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class IsNotGithubUrlException extends BusinessException {

	private static final String FAIL_CODE = "1011";

	public IsNotGithubUrlException() {
		super(FAIL_CODE, HttpStatus.NOT_ACCEPTABLE);
	}

	@Override
	public String getMessage() {
		return "URL 형식에 맞지 않습니다.";
	}
}
