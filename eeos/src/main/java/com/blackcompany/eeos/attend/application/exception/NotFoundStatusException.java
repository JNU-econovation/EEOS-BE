package com.blackcompany.eeos.attend.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class NotFoundStatusException extends BusinessException {

	public NotFoundStatusException() {
		super("존재하지 않는 상태입니다.", HttpStatus.NOT_FOUND);
	}
}
