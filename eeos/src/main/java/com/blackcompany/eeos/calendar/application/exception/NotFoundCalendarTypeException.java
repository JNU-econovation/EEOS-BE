package com.blackcompany.eeos.calendar.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NotFoundCalendarTypeException extends BusinessException {

	private static final String FAIL_CODE = "";

	public NotFoundCalendarTypeException() {
		super(FAIL_CODE, HttpStatus.BAD_REQUEST);
	}

	@Override
	public String getMessage() {
		return "칼렌더 타입이 존재하지 않습니다.";
	}
}
