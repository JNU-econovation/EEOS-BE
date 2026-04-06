package com.blackcompany.eeos.member.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

/** 대상 회원이 Slack 전용 회원이 아닐 때 발생하는 예외 */
public class NotSlackOnlyMemberException extends BusinessException {
	private static final String FAIL_CODE = "4203";

	public NotSlackOnlyMemberException() {
		super(FAIL_CODE, HttpStatus.CONFLICT);
	}

	@Override
	public String getMessage() {
		return "Slack 전용 회원이 아닙니다.";
	}
}
