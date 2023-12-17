package com.blackcompany.eeos.attend.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

/** 존재하지 않는 참석 상태일 때 발생하는 예외 */
public class NotFoundAttendStatusException extends BusinessException {
	private static final String FAIL_CODE = "2000";
	private final String attendStatus;

	public NotFoundAttendStatusException(String attendStatus) {
		super(FAIL_CODE, HttpStatus.NOT_FOUND);
		this.attendStatus = attendStatus;
	}

	@Override
	public String getMessage() {
		return String.format("%s 참석 상태는 존재하지 않습니다.", attendStatus);
	}
}
