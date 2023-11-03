package com.blackcompany.eeos.attend.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class NotSameBeforeStatusException extends BusinessException {

	public NotSameBeforeStatusException() {
		super("이전 참석 상태 정보가 올바르지 않습니다.", HttpStatus.BAD_REQUEST);
	}
}
