package com.blackcompany.eeos.member.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

/** 존재하지 않는 멤버일 때 발생하는 예외 */
public class NotFoundMemberException extends BusinessException {
	private static final String FAIL_CODE = "3000";

	public NotFoundMemberException() {
		super(FAIL_CODE, HttpStatus.NOT_FOUND);
	}

	@Override
	public String getMessage() {
		return "존재하지 않는 멤버입니다.";
	}
}
