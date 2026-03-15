package com.blackcompany.eeos.slackEvent.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class SlackForwardFailedException extends BusinessException {

	private static final String CODE = "5013";
	private final String eventId;

	public SlackForwardFailedException(String eventId, Throwable cause) {
		super(CODE, HttpStatus.INTERNAL_SERVER_ERROR);
		this.eventId = eventId;
		initCause(cause);
	}

	@Override
	public String getMessage() {
		return String.format("Slack 이벤트 전달에 실패했습니다. eventId=%s", eventId);
	}
}
