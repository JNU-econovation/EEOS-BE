package com.blackcompany.eeos.team.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class NotFoundTeamStatusException extends BusinessException {
	private static final String FAIL_CODE = "7001";
	private final String status;

	public NotFoundTeamStatusException(String status) {
		super(FAIL_CODE, HttpStatus.NOT_FOUND);
		this.status = status;
	}

	@Override
	public String getMessage() {
		return String.format("%s는 올바르지 않는 형식의 팀 상태 입니다.", status);
	}
}
