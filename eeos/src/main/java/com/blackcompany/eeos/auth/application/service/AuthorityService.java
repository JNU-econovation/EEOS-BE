package com.blackcompany.eeos.auth.application.service;

import com.blackcompany.eeos.auth.application.model.AuthorityModel;
import com.blackcompany.eeos.auth.application.model.Role;
import com.blackcompany.eeos.auth.application.repository.AuthorityRepository;
import com.blackcompany.eeos.auth.application.usecase.AuthorityUsecase;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorityService implements AuthorityUsecase {

    private final AuthorityRepository authorityRepository;

    @Override
    public void changeRole(Long memberId, String from, String to) {
        Role from2 = Role.findOrganizationRole(from);
        Role to2 = Role.findOrganizationRole(to);

        AuthorityModel model = authorityRepository.findByIdAndRole(memberId, from2);

        model.updateRole(to2);
        authorityRepository.save(model);
    }

    @Override
    public List<Role> getOrganizationRoles() {
        return Role.getOrganizationRoles();
    }
}
