package com.blackcompany.eeos.auth.application.model;

import com.blackcompany.eeos.common.support.AbstractModel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
public class AuthorityModel implements AbstractModel {

	private final Long id;
	private final Long memberId;
	private final Role role;

	public static AuthorityModel create(Long memberId, Role role) {
		return AuthorityModel.builder().role(role).memberId(memberId).build();
	}
}
