package com.blackcompany.eeos.program.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class NotFoundProgramStatusException extends BusinessException {
	public NotFoundProgramStatusException() {
		super("존재하지 않는 프로그램 상태 입니다.", HttpStatus.NOT_FOUND);
	}
}
