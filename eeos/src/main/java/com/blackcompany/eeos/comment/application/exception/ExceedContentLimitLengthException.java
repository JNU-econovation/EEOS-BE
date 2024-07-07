package com.blackcompany.eeos.comment.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class ExceedContentLimitLengthException extends BusinessException {

    private static final String FAIL_CODE = "";

    private final long contentLength;

    public ExceedContentLimitLengthException(long contentLength){
        super(FAIL_CODE, HttpStatus.FORBIDDEN);
        this.contentLength = contentLength;
    }

    @Override
    public String getMessage() { return String.format("코멘트 제한 글자 수를 초과하였습니다. (글자 수 : %d)", contentLength); }

}
