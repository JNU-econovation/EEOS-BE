package com.blackcompany.eeos.calendar.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import com.blackcompany.eeos.member.application.model.Department;
import org.springframework.http.HttpStatus;

public class DeniedCalendarTypeException extends BusinessException {

    private static final String FAIL_CODE = "10001";
    private final Department department;

    public DeniedCalendarTypeException(Department department){
        super(FAIL_CODE, HttpStatus.BAD_REQUEST);
        this.department = department;
    }

    @Override
    public String getMessage() { return String.format("%s는 해당 타입의 칼렌더를 생성할 수 없습니다.", department.getKoName()); }

}
