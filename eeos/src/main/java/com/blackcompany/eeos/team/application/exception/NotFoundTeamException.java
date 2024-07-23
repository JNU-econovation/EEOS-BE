package com.blackcompany.eeos.team.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class NotFoundTeamException extends BusinessException {
	private static final String FAIL_CODE = "7000";

	public NotFoundTeamException() {
		super(FAIL_CODE, HttpStatus.BAD_REQUEST);
	}

	@Override
	public String getMessage(){ return "존재하지 않는 팀입니다."; }

}
