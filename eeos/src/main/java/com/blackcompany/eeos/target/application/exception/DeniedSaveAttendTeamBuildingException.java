package com.blackcompany.eeos.target.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class DeniedSaveAttendTeamBuildingException extends BusinessException {
	private static final String FAIL_CODE = "6008";

	public DeniedSaveAttendTeamBuildingException() {
		super(FAIL_CODE, HttpStatus.BAD_REQUEST);
	}

	@Override
	public String getMessage() {
		return String.format("팀빌딩이 종료되어 팀빌딩 입력사항을 변경할 수 없습니다.");
	}
}
