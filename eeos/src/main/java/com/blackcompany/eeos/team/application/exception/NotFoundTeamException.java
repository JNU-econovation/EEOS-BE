package com.blackcompany.eeos.team.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class NotFoundTeamException extends BusinessException {
	private static final String FAIL_CODE = "7000";
	private final Long teamId;

	public NotFoundTeamException(Long teamId) {
		super(FAIL_CODE, HttpStatus.BAD_REQUEST);
		this.teamId = teamId;
	}

	@Override
	public String getMessage() {
		return String.format(" teamId %d는 존재하지 않는 팀입니다.", teamId);
	}

}
