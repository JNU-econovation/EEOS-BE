package com.blackcompany.eeos.comment.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class NotCreateAdminCommentException extends BusinessException {

    private static final String FAIL_CODE = "";

    public NotCreateAdminCommentException(){
        super(FAIL_CODE, HttpStatus.BAD_REQUEST);
    }

    @Override
    public String getMessage() { return "관리자는 코멘트를 작성할 수 없습니다."; }

}
