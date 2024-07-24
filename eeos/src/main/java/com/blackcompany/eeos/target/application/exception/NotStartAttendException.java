package com.blackcompany.eeos.target.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class NotStartAttendException extends BusinessException {

    private static final String FAIL_CODE = "2007";

    public NotStartAttendException(){
        super(FAIL_CODE, HttpStatus.BAD_REQUEST);
    }

    @Override
    public String getMessage(){ return "출석체크가 종료되었습니다."; }

}
