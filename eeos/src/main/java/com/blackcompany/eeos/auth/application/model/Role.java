package com.blackcompany.eeos.auth.application.model;

import java.util.Arrays;
import java.util.List;
import lombok.Getter;

@Getter
public enum Role {
	ROLE_ADMIN(1L, "ADMIN"),
	ROLE_USER(2L, "USER");

	private final Long id;
	private final String role; // 시스템 내에 존재하는 role을 찾을 때, 이 문자열을 기준으로 찾습니다.

	Role(Long id, String role) {
		this.id = id;
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

	public static Role findById(Long id){
		return Arrays.stream(Role.values())
				.filter(obj -> obj.getId().equals(id))
				.findFirst()
				.orElseThrow(IllegalArgumentException::new);
	}

	public static List<Role> getAllRoles() {
		return Arrays.asList(Role.values());
	}
}
