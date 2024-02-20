package com.blackcompany.eeos.teamBuilding.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

/** 조회할 팀빌딩이 존재하지 않을 때 발생하는 예외 */
public class NotFoundResultException extends BusinessException {
	private static final String FAIL_CODE = "6009";

	public NotFoundResultException() {
		super(FAIL_CODE, HttpStatus.NOT_FOUND);
	}

	@Override
	public String getMessage() {
		return "결과를 조회할 팀빌딩이 존재하지 않습니다.";
	}
}
