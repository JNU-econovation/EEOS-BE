package com.blackcompany.eeos.teamBuilding.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

/** 요청한 상태의 팀빌딩이 존재하지 않을 때 발생하는 예외 */
public class NotFoundTeamBuildingStatusException extends BusinessException {
	private static final String FAIL_CODE = "6002";
	private final String status;

	public NotFoundTeamBuildingStatusException(String status) {
		super(FAIL_CODE, HttpStatus.NOT_FOUND);
		this.status = status;
	}

	@Override
	public String getMessage() {
		return String.format("%s 인 팀빌딩이 존재하지 않습니다.", status);
	}
}
