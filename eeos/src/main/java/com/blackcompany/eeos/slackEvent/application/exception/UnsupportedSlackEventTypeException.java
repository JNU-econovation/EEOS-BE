package com.blackcompany.eeos.slackEvent.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class UnsupportedSlackEventTypeException extends BusinessException {

	private static final String CODE = "5014";

	public UnsupportedSlackEventTypeException(String type) {
		super(CODE, HttpStatus.BAD_REQUEST);
		this.type = type;
	}

	private final String type;

	@Override
	public String getMessage() {
		return "지원하지 않는 Slack 이벤트 타입입니다: " + type;
	}
}
