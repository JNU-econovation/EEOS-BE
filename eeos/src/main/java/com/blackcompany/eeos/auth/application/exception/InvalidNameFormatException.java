package com.blackcompany.eeos.auth.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

/** 멤버의 이름 형식이 올바르지 않을 떄 발생하는 예외 */
public class InvalidNameFormatException extends BusinessException {
	private static final String FAIL_CODE = "4004";
	private final String name;

	public InvalidNameFormatException(String name) {
		super(FAIL_CODE, HttpStatus.BAD_REQUEST);
		this.name = name;
	}

	@Override
	public String getMessage() {
		return String.format("%s 멤버의 이름이 형식과 일치하지 않습니다.", name);
	}
}
