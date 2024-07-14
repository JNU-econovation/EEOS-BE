package com.blackcompany.eeos.comment.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class ExceedContentLimitLengthException extends BusinessException {

	private static final String FAIL_CODE = "8005";

	public ExceedContentLimitLengthException() {
		super(FAIL_CODE, HttpStatus.FORBIDDEN);
	}

	@Override
	public String getMessage() {
		return "코멘트 글자 수 제한을 초과하였습니다.";
	}
}
