package com.blackcompany.eeos.teamBuilding.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

/** 팀빌딩 활성 횟수를 넘겼을 때 발생하는 예외 */
public class DeniedOverUpperLimitException extends BusinessException {
	private static final String FAIL_CODE = "6000";

	public DeniedOverUpperLimitException() {
		super(FAIL_CODE, HttpStatus.BAD_REQUEST);
	}

	@Override
	public String getMessage() {
		return "진행중인 팀빌딩이 존재합니다.";
	}
}
