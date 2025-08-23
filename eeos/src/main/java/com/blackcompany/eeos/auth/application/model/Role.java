package com.blackcompany.eeos.auth.application.model;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum Role {
	ROLE_ADMIN("ADMIN"),
	ROLE_USER("USER"),
	ROLE_PRESIDENT("PRESIDENT"),
	ROLE_DPT_MARKETING("MARKETING"),
	ROLE_DPT_MANAGEMENT("MANAGEMENT"),
	ROLE_DPT_EVENT("EVENT");

	private String role; // 시스템 내에 존재하는 role을 찾을 때, 이 문자열을 기준으로 찾습니다.

	Role(String role) {
		this.role = role;
	}

	public static boolean isExist(String role){
		return Arrays.stream(Role.values())
				.anyMatch(obj -> obj.getRole().equals(role));
	}

	public static Role findRole(String role){
		return Arrays.stream(Role.values())
				.filter(obj -> obj.getRole().equals(role))
				.findFirst()
				.orElseThrow(IllegalArgumentException::new);
	}
}
