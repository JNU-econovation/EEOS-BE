package com.blackcompany.eeos.teamBuilding.application.exception;

import org.springframework.http.HttpStatus;

public class EndTeamBuildingException extends NotFoundProgressTeamBuildingException {
	private static final String FAIL_CODE = "6011";

	public EndTeamBuildingException() {
		super(FAIL_CODE, HttpStatus.BAD_REQUEST);
	}

	@Override
	public String getMessage() {
		return "팀빌딩이 종료되었습니다.";
	}
}
