package com.blackcompany.eeos.member.application.dto;

import com.blackcompany.eeos.common.support.dto.AbstractResponseDto;
import com.blackcompany.eeos.member.application.model.Department;

public record DepartmentResponse(Long departmentId, String enName, String koName)
		implements AbstractResponseDto {

	public static DepartmentResponse from(Department department) {
		return new DepartmentResponse(
				department.getId(), department.getEnName(), department.getKoName());
	}
}
