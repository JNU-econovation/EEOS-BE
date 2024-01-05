package com.blackcompany.eeos.program.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

/** 존재하지 않는 프로그램 카테고리일 때 발생하는 예외 */
public class NotFoundProgramCategoryException extends BusinessException {
	private static final String FAIL_CODE = "1002";
	private final String category;

	public NotFoundProgramCategoryException(String category) {
		super(FAIL_CODE, HttpStatus.NOT_FOUND);
		this.category = category;
	}

	@Override
	public String getMessage() {
		return String.format("%s 는 존재하지 않는 프로그램 카테고리 입니다.", category);
	}
}
