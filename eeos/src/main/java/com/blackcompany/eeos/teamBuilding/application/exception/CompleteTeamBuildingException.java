package com.blackcompany.eeos.teamBuilding.application.exception;

import org.springframework.http.HttpStatus;

public class CompleteTeamBuildingException extends NotFoundProgressTeamBuildingException {
	private static final String FAIL_CODE = "6010";

	public CompleteTeamBuildingException() {
		super(FAIL_CODE, HttpStatus.UNAUTHORIZED);
	}

	@Override
	public String getMessage() {
		return "팀빌딩이 완료되었습니다.";
	}
}
