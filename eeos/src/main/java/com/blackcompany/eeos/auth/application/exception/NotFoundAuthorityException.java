package com.blackcompany.eeos.auth.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class NotFoundAuthorityException extends BusinessException {

    private static final String FAIL_CODE = "4013";

    public NotFoundAuthorityException(){
        super(FAIL_CODE, HttpStatus.NOT_FOUND);
    }

    @Override
    public String getMessage() {
        return "회원의 권한을 찾을 수 없습니다.";
    }

}
