package com.blackcompany.eeos.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException {
	private final String code;
	private final HttpStatus httpStatus;

	public BusinessException(String code, HttpStatus httpStatus) {
		this.code = code;
		this.httpStatus = httpStatus;
	}

	public BusinessException(String code, HttpStatus httpStatus, Throwable cause) {
		super(cause);
		this.code = code;
		this.httpStatus = httpStatus;
	}
}
