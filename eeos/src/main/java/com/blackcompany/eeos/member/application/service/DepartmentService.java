package com.blackcompany.eeos.member.application.service;

import com.blackcompany.eeos.member.application.model.Department;
import com.blackcompany.eeos.member.application.model.MemberModel;
import com.blackcompany.eeos.member.application.repository.MemberRepository;
import com.blackcompany.eeos.member.application.usecase.DepartmentUsecase;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DepartmentService implements DepartmentUsecase {

    private final MemberRepository memberRepository;

    @Override
    public void changeDepartment(Long memberId, String to) {
        MemberModel member = memberRepository.findById(memberId);
        Department depart = Department.findDepartmentByEnName(to);
        member.updateDepartment(depart);
        memberRepository.save(member);
    }

    public List<Department> getAllDepartments() {
        return Department.getAllDepartments();
    }

    public Department getDepartment(String name) {
        return Department.findDepartmentByEnName(name);
    }

    public Department getDepartmentById(Long id) {
        return Department.findById(id);
    }
}