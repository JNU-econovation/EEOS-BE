package com.blackcompany.eeos.teamBuilding.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

/** 진행중인 팀빌딩이 존재하지 않을 때 예외 */
public class NotFoundProgressTeamBuildingException extends BusinessException {
	private static final String FAIL_CODE = "6002";

	public NotFoundProgressTeamBuildingException() {
		super(FAIL_CODE, HttpStatus.NOT_FOUND);
	}

	@Override
	public String getMessage() {
		return "진행중인 팀빌딩이 존재하지 않습니다";
	}
}
