package com.blackcompany.eeos.comment.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class DeniedDeleteCommentException extends BusinessException {

    private static final String FAIL_CODE = "8003";

    private final Long commentId;

    public DeniedDeleteCommentException(Long commentId){
        super(FAIL_CODE, HttpStatus.NOT_ACCEPTABLE);
        this.commentId = commentId;
    }

    @Override
    public String getMessage(){ return String.format("코멘트 편집 권한이 없는 사용자입니다. (commentId : %d)", commentId); }

}
