package com.blackcompany.eeos.target.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class DeniedChangeAttendException extends BusinessException {

    private static final String FAIL_CODE = "2006";

    public DeniedChangeAttendException(){
        super(FAIL_CODE, HttpStatus.BAD_REQUEST);
    }

    @Override
    public String getMessage(){ return "출석체크는 1회만 가능합니다."; }

}
