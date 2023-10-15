package com.blackcompany.eeos.program.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class OverDateException extends BusinessException {

	public OverDateException(String message) {
		super(message, HttpStatus.BAD_REQUEST);
	}
}
