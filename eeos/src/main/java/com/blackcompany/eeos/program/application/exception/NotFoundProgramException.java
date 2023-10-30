package com.blackcompany.eeos.program.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class NotFoundProgramException extends BusinessException {
	public NotFoundProgramException() {
		super("존재하지 않는 프로그램입니다.", HttpStatus.NOT_FOUND);
	}
}
