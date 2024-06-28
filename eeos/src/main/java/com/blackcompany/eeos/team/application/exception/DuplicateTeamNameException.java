package com.blackcompany.eeos.team.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class DuplicateTeamNameException extends BusinessException {
	private static final String FAIL_CODE = "7002";
	private final String team_name;

	public DuplicateTeamNameException(String team_name) {
		super(FAIL_CODE, HttpStatus.NOT_FOUND);
		this.team_name = team_name;
	}

	@Override
	public String getMessage() {
		return String.format("%s 팀은 이미 등록되어있는 팀입니다.", team_name);
	}
}
