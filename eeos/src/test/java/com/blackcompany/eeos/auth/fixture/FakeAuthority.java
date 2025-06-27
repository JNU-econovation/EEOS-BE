package com.blackcompany.eeos.auth.fixture;

import com.blackcompany.eeos.auth.application.model.AuthorityModel;

public class FakeAuthority {

    public static AuthorityModel authorityModel(Long id, Long memberId, String role){
        return AuthorityModel.builder()
                .id(id)
                .memberId(memberId)
                .name(role)
                .build();
    }

}
