package com.blackcompany.eeos.program.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;
/**
 * 존재하지 않는 프로그램일 때 발생하는 예외
 */
public class NotFoundProgramException extends BusinessException {
	private static final String FAIL_CODE = "1000";
	private final Long programId;

	public NotFoundProgramException(Long programId) {
		super(FAIL_CODE, HttpStatus.NOT_FOUND);
		this.programId = programId;
	}

	@Override
	public String getMessage() {
		return String.format("존재하지 않는 프로그램입니다. programId : %d", programId);
	}
}
