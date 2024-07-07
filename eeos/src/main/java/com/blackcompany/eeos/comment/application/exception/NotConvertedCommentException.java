package com.blackcompany.eeos.comment.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class NotConvertedCommentException extends BusinessException {

	private static final String FAIL_CODE = "8002";

	public NotConvertedCommentException() {
		super(FAIL_CODE, HttpStatus.EXPECTATION_FAILED);
	}

	@Override
	public String getMessage() {
		return "코멘트 응답을 생성하는 동안에 예상치 못한 오류가 발생했습니다.";
	}
}
