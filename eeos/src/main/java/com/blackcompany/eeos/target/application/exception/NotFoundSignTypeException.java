package com.blackcompany.eeos.target.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

/** 존재하지 않는 참석 상태일 때 발생하는 예외 */
public class NotFoundSignTypeException extends BusinessException {
	private static final String FAIL_CODE = "2000";
	private final String type;

	public NotFoundSignTypeException(String type) {
		super(FAIL_CODE, HttpStatus.NOT_FOUND);
		this.type = type;
	}

	@Override
	public String getMessage() {
		return String.format("%s 존재하지 않는 존재하지 않습니다.", type);
	}
}
