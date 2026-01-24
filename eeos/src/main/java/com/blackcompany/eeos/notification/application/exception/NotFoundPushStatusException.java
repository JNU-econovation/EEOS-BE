package com.blackcompany.eeos.notification.application.exception;

import org.springframework.http.HttpStatus;

import com.blackcompany.eeos.common.exception.BusinessException;

public class NotFoundPushStatusException extends BusinessException {

	private static final String FAIL_CODE = "5007";
	private final String status;

	public NotFoundPushStatusException(String status) {

		super(FAIL_CODE, HttpStatus.NOT_FOUND);
		this.status = status;
	}

	@Override
	public String getMessage(){
		return String.format("%s 는 존재하지 않는 알림상태 입니다.", status);
	}
}
