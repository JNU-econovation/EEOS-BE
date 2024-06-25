package com.blackcompany.eeos.team.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class NotFoundTeamException extends BusinessException {
	private static final String FAIL_CODE = "7000";
	private final Long TeamId;

	public NotFoundTeamException(Long teamId) {
		super(FAIL_CODE, HttpStatus.NOT_FOUND);
		this.TeamId = teamId;
	}
}
