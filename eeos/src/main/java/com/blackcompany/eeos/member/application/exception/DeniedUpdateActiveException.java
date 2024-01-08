package com.blackcompany.eeos.member.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

/** 활동 상태 변경이 불가능할 때 발생하는 예외 */
public class DeniedUpdateActiveException extends BusinessException {
	private static final String FAIL_CODE = "3002";
	private final String status;

	public DeniedUpdateActiveException(String status) {
		super(FAIL_CODE, HttpStatus.FORBIDDEN);
		this.status = status;
	}

	@Override
	public String getMessage() {
		return String.format("%s 활동상태로 변경은 금지되었습니다..", status);
	}
}
