package com.blackcompany.eeos.slackEvent.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class SlackReplayAttackException extends BusinessException {

	private static final String CODE = "5012";

	public SlackReplayAttackException() {
		super(CODE, HttpStatus.UNAUTHORIZED);
	}

	@Override
	public String getMessage() {
		return "허용 시간을 초과한 Slack 요청입니다.";
	}
}
