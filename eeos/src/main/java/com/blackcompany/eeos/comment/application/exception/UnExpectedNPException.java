package com.blackcompany.eeos.comment.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class UnExpectedNPException extends BusinessException {

    private static final String FAIL_CODE = "";

    public UnExpectedNPException(){
        super(FAIL_CODE, HttpStatus.EXPECTATION_FAILED);
    }

    @Override
    public String getMessage() {
        return "코멘트 생성 중에 예상치 못한 에러가 발생하였습니다.(NPE)";
    }

}
