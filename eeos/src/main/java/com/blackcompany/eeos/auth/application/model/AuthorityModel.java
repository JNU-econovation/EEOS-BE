package com.blackcompany.eeos.auth.application.model;

import com.blackcompany.eeos.common.support.AbstractModel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthorityModel implements AbstractModel {

	private final Long id;
	private final Long memberId;
	private Role role;

	public AuthorityModel(Long id, Long memberId, Role role) {
		this.id = id;
		this.memberId = memberId;
		this.role = role;
	}

	public void updateRole(Role role){
		this.role = role;
	}

	public static AuthorityModel create(Long memberId, Role role) {
		return AuthorityModel.builder().role(role).memberId(memberId).build();
	}
}
