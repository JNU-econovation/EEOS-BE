package com.blackcompany.eeos.program.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

/** 해당 프로그램에 편집(수정, 삭제) 권한이 없을 때 발생하는 예외 */
public class DeniedProgramEditException extends BusinessException {
	private static final String FAIL_CODE = "1005";
	private final Long programId;

	public DeniedProgramEditException(Long programId) {
		super(FAIL_CODE, HttpStatus.NOT_FOUND);
		this.programId = programId;
	}

	@Override
	public String getMessage() {
		return String.format("%s 프로그램 편집 권한(생성/수정/삭제)이 없는 사용자입니다.", programId);
	}
}
