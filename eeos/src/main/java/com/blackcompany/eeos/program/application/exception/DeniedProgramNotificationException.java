package com.blackcompany.eeos.program.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class DeniedProgramNotificationException extends BusinessException {

	private static final String FAIL_CODE = "1008";
	private final long memberId;

	public DeniedProgramNotificationException(long memberId) {
		super(FAIL_CODE, HttpStatus.BAD_REQUEST);
		this.memberId = memberId;
	}

	@Override
	public String getMessage() {
		return String.format("슬랙 알림은 행사 생성자만 가능합니다.(요청 멤버 ID : %s)", memberId);
	}
}
