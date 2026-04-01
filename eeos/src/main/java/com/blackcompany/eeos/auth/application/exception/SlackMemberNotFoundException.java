package com.blackcompany.eeos.auth.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class SlackMemberNotFoundException extends BusinessException {

	private static final String FAIL_CODE = "4200";

	public SlackMemberNotFoundException() {
		super(FAIL_CODE, HttpStatus.NOT_FOUND);
	}

	@Override
	public String getMessage() {
		return "해당 Slack 회원을 찾을 수 없습니다.";
	}
}
