package com.blackcompany.eeos.team.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class DeniedTeamEditException extends BusinessException {
	private static final String FAIL_CODE = "7003";
	private final Long memberId;

	public DeniedTeamEditException(Long memberId) {
		super(FAIL_CODE, HttpStatus.NOT_FOUND);
		this.memberId = memberId;
	}

	@Override
	public String getMessage() {
		return String.format("%s 은 팀 편집 권한(생성/삭제)이 없는 사용자 입니다.", memberId);
	}
}
