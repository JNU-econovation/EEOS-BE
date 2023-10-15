package com.blackcompany.eeos.program.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class NotFoundProgramException extends BusinessException {
	public NotFoundProgramException(String message) {
		super(message, HttpStatus.NOT_FOUND);
	}
}
