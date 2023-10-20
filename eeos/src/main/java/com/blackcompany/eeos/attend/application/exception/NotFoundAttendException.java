package com.blackcompany.eeos.attend.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class NotFoundAttendException extends BusinessException {

	public NotFoundAttendException() {
		super("존재하지 않는 참석 정보입니다.", HttpStatus.NOT_FOUND);
	}
}
