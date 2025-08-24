package com.blackcompany.eeos.member.application.dto;

import com.blackcompany.eeos.member.application.model.Department;
import com.blackcompany.eeos.common.support.dto.AbstractResponseDto;

public record DepartmentResponse(
        Long departmentId,
        String name
) implements AbstractResponseDto {

    public static DepartmentResponse from(Department department){
        return new DepartmentResponse(department.getId(), department.getName());
    }

}
