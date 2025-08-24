package com.blackcompany.eeos.member.application.dto;

import com.blackcompany.eeos.common.support.dto.AbstractRequestDto;
import com.blackcompany.eeos.member.application.model.Department;
import jakarta.validation.constraints.AssertTrue;

public record ChangeDepartmentRequest(
        Long memberId,
        String to
) implements AbstractRequestDto {

    @AssertTrue(message = "전달받은 department 가 존재하지 않습니다.")
    public boolean existsDepartment(){
        return Department.isExist(to);
    }

}
