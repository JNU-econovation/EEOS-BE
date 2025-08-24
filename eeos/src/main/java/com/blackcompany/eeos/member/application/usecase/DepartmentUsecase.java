package com.blackcompany.eeos.member.application.usecase;

import com.blackcompany.eeos.member.application.model.Department;
import java.util.List;

public interface DepartmentUsecase {

    void changeDepartment(Long memberId, String to);

    List<Department> getAllDepartments();

}
