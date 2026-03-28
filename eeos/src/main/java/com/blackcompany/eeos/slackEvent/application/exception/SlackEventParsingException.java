package com.blackcompany.eeos.slackEvent.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class SlackEventParsingException extends BusinessException {

	private static final String CODE = "5016";

	public SlackEventParsingException() {
		super(CODE, HttpStatus.BAD_REQUEST);
	}

	@Override
	public String getMessage() {
		return "Slack 이벤트 요청을 파싱할 수 없습니다.";
	}
}
