package com.blackcompany.eeos.comment.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class NotFoundCommentException extends BusinessException {

	public static final String FAIL_CODE = "8000";
	private Long commentId;

	public NotFoundCommentException(Long commentId) {
		super(FAIL_CODE, HttpStatus.NOT_FOUND);
		this.commentId = commentId;
	}

	@Override
	public String getMessage() {
		return "존재하지 않는 코멘트입니다. : " + commentId;
	}
}
