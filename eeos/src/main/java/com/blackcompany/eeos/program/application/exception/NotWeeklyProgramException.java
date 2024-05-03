package com.blackcompany.eeos.program.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class NotWeeklyProgramException extends BusinessException {

    private static final String FAIL_CODE = "1009";

    public NotWeeklyProgramException(){
        super(FAIL_CODE, HttpStatus.BAD_REQUEST);
    }

    @Override
    public String getMessage() { return "주간발표 행사만 알림을 요청할 수 있습니다.";}
}
