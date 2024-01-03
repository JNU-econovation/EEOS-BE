package com.blackcompany.eeos.member.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

/** 존재하지 않는 활동 상태일 때 발생하는 예외 */
public class NotFoundActiveStatusException extends BusinessException {
	private static final String FAIL_CODE = "3001";
	private final String status;

	public NotFoundActiveStatusException(String status) {
		super(FAIL_CODE, HttpStatus.NOT_FOUND);
		this.status = status;
	}

	@Override
	public String getMessage() {
		return String.format("%s 는 존재하지 않는 활동 상태입니다.", status);
	}
}
