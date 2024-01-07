package com.blackcompany.eeos.program.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

/** 진행중인 프로그램의 참여 대상자를 수정하려고 할 때 발생하는 예외 */
public class NotAllowedUpdatedProgramAttendException extends BusinessException {
	private static final String FAIL_CODE = "1007";
	private final Long programId;

	public NotAllowedUpdatedProgramAttendException(Long programId) {
		super(FAIL_CODE, HttpStatus.BAD_REQUEST);
		this.programId = programId;
	}

	@Override
	public String getMessage() {
		return String.format("진행중인 %s 프로그램의 참여 대상자는 수정할 수 없습니다.", programId);
	}
}
