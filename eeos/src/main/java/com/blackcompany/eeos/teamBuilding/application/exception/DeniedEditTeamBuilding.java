package com.blackcompany.eeos.teamBuilding.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

/** 팀빌딩 수정 권한이 없을 때 발생하는 예외 */
public class DeniedEditTeamBuilding extends BusinessException {
	private static final String FAIL_CODE = "6005";

	public DeniedEditTeamBuilding() {
		super(FAIL_CODE, HttpStatus.UNAUTHORIZED);
	}

	@Override
	public String getMessage() {
		return "팀빌딩 수정 권한을 가지고 있지 않습니다.";
	}
}
