package com.blackcompany.eeos.attend.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

/**
 * 존재하지 않는 참석 정보일 때 발생하는 예외
 */
public class NotFoundAttendException extends BusinessException {
    private static final String FAIL_CODE = "2004";
    private final Long programId;

    public NotFoundAttendException(Long programId) {
        super(FAIL_CODE, HttpStatus.NOT_FOUND);
        this.programId = programId;
    }

    @Override
    public String getMessage() {
        return String.format("%s 프로그램에 대한 참여 정보가 없습니다.", programId);
    }
}
