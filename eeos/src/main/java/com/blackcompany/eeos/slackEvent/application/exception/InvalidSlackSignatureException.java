package com.blackcompany.eeos.slackEvent.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class InvalidSlackSignatureException extends BusinessException {

	private static final String CODE = "5011";

	public InvalidSlackSignatureException() {
		super(CODE, HttpStatus.UNAUTHORIZED);
	}

	@Override
	public String getMessage() {
		return "유효하지 않은 Slack 서명입니다.";
	}
}
