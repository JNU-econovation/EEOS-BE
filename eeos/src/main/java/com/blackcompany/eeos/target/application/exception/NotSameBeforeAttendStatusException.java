package com.blackcompany.eeos.target.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

/** 이전 참석상태가 일치하지 않을 때 발생하는 예외 */
public class NotSameBeforeAttendStatusException extends BusinessException {
	private static final String FAIL_CODE = "2001";
	private final Long memberId;

	public NotSameBeforeAttendStatusException(Long memberId) {
		super(FAIL_CODE, HttpStatus.NOT_FOUND);
		this.memberId = memberId;
	}

	@Override
	public String getMessage() {
		return String.format("%s 회원의 이전 상태가 올바르지 않습니다.", memberId);
	}
}
