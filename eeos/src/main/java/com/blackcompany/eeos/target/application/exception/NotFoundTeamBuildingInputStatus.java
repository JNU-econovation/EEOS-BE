package com.blackcompany.eeos.target.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

/** 존재하지 않는 팀빌딩 입력 상태일 때 발생하는 예외 */
public class NotFoundTeamBuildingInputStatus extends BusinessException {
	private static final String FAIL_CODE = "6007";
	private final String status;

	public NotFoundTeamBuildingInputStatus(String status) {
		super(FAIL_CODE, HttpStatus.NOT_FOUND);
		this.status = status;
	}

	@Override
	public String getMessage() {
		return String.format("%s는 존재하지 않는 팀빌딩 입력 상태입니다.", status);
	}
}
