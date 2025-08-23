package com.blackcompany.eeos.auth.application.service;

import com.blackcompany.eeos.auth.application.model.AuthorityModel;
import com.blackcompany.eeos.auth.application.model.Role;
import com.blackcompany.eeos.auth.application.repository.AuthorityRepository;
import com.blackcompany.eeos.auth.application.usecase.AuthorityUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorityService implements AuthorityUsecase {

    private final AuthorityRepository authorityRepository;

    @Override
    public void changeRole(Long memberId, String from, String to) {
        Role from2 = Role.findRole(from);
        AuthorityModel model = authorityRepository.findByIdAndRole(memberId, from2);
        Role to2 = Role.findRole(to);
        model.updateRole(to2);
        authorityRepository.save(model);
    }
}
