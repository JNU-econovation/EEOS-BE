package com.blackcompany.eeos.auth.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class InvalidRedirectUriException extends BusinessException {
	private static final String FAIL_CODE = "4015";

	public InvalidRedirectUriException() {
		super(FAIL_CODE, HttpStatus.BAD_REQUEST);
	}

	@Override
	public String getMessage() {
		return "등록되지 않은 redirect URI입니다.";
	}
}
