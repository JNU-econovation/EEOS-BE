package com.blackcompany.eeos.slackEvent.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class InvalidSlackEventIdException extends BusinessException {

	private static final String CODE = "5015";

	public InvalidSlackEventIdException() {
		super(CODE, HttpStatus.BAD_REQUEST);
	}

	@Override
	public String getMessage() {
		return "유효하지 않은 Slack 이벤트 ID입니다.";
	}
}
