package com.blackcompany.eeos.auth.persistence;

import org.springframework.stereotype.Component;

@Component
public class AuthInfoEntityConverter {

	public AuthInfoEntity from(Long memberId, String refreshToken) {
		return AuthInfoEntity.builder().memberId(memberId).token(refreshToken).build();
	}
}
