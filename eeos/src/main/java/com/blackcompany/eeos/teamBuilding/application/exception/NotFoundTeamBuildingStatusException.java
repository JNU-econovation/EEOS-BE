package com.blackcompany.eeos.teamBuilding.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

/** 존재하지 않는 팀빌딩 타입일 때 발생하는 예외 */
public class NotFoundTeamBuildingStatusException extends BusinessException {
	private static final String FAIL_CODE = "1003";
	private final String status;

	public NotFoundTeamBuildingStatusException(String status) {
		super(FAIL_CODE, HttpStatus.NOT_FOUND);
		this.status = status;
	}

	@Override
	public String getMessage() {
		return String.format("%s 는 존재하지 않는 팀빌딩 상태입니다.", status);
	}
}
