package com.blackcompany.eeos.comment.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class UpdateFailedCommentException extends BusinessException {

	public static final String FAIL_CODE = "8001";

	private Long commentId;

	public UpdateFailedCommentException(Long commentId) {
		super(FAIL_CODE, HttpStatus.EXPECTATION_FAILED);
		this.commentId = commentId;
	}

	public String getMessage() {
		return "코멘트 수정에 실패하였습니다. : " + commentId;
	}
}
