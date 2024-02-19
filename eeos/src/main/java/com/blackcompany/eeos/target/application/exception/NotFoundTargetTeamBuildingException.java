package com.blackcompany.eeos.target.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

/** 팀빌딩 대상자가 아닐 때 발생하는 예외 */
public class NotFoundTargetTeamBuildingException extends BusinessException {
	private static final String FAIL_CODE = "6003";

	public NotFoundTargetTeamBuildingException() {
		super(FAIL_CODE, HttpStatus.NOT_FOUND);
	}

	@Override
	public String getMessage() {
		return "팀빌딩 대상자가 아닙니다.";
	}
}
