package com.blackcompany.eeos.auth.fixture;

import com.blackcompany.eeos.auth.persistence.AuthInfoEntity;

public class FakeAuthInfo {
	public static AuthInfoEntity authInfoEntity() {
		return AuthInfoEntity.builder().memberId(1L).token("token").build();
	}
}
