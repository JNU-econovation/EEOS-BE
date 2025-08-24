package com.blackcompany.eeos.member.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class NotFoundDepartmentException extends BusinessException {

    private static final String FAIL_CODE = "3003";

    public NotFoundDepartmentException(){
        super(FAIL_CODE, HttpStatus.NOT_FOUND);
    }

    @Override
    public String getMessage() {
        return "해당 부서를 찾을 수 없습니다.";
    }

}
