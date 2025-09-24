package com.blackcompany.eeos.auth.fixture;

import com.blackcompany.eeos.auth.application.model.AuthorityModel;
import com.blackcompany.eeos.auth.application.model.Role;

public class FakeAuthority {

	public static AuthorityModel authorityModel(Long id, Long memberId, Role role) {
		return AuthorityModel.builder().id(id).memberId(memberId).role(role).build();
	}
}
