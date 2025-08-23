package com.blackcompany.eeos.auth.application.model;

import java.util.Arrays;
import java.util.List;
import lombok.Getter;

@Getter
public enum Role {
	// System
	ROLE_ADMIN(1L, "ADMIN", RoleType.SYSTEM),
	ROLE_USER(2L, "USER", RoleType.SYSTEM),

	// Organization
	ROLE_PRESIDENT(101L, "PRESIDENT", RoleType.ORGANIZATION),
	ROLE_DPT_MARKETING(102L, "MARKETING", RoleType.ORGANIZATION),
	ROLE_DPT_MANAGEMENT(103L, "MANAGEMENT", RoleType.ORGANIZATION),
	ROLE_DPT_EVENT(104L, "EVENT", RoleType.ORGANIZATION),
	ROLE_DPT_NONE(105L, "NONE", RoleType.ORGANIZATION);

	private final Long id;
	private final String role; // 시스템 내에 존재하는 role을 찾을 때, 이 문자열을 기준으로 찾습니다.
	private final RoleType roleType;

	Role(Long id, String role, RoleType roleType) {
		this.id = id;
		this.role = role;
		this.roleType = roleType;
	}

	public static boolean isExist(String role){
		return Arrays.stream(Role.values())
				.anyMatch(obj -> obj.getRole().equals(role));
	}

	public static Role findOrganizationRole(String role){
		return Arrays.stream(Role.values())
				.filter(obj -> obj.getRole().equals(role) && obj.isOrganizationRole())
				.findFirst()
				.orElseThrow(IllegalArgumentException::new);
	}

	public static Role findSystemRole(String role){
		return Arrays.stream(Role.values())
				.filter(obj -> obj.getRole().equals(role) && obj.isSystemRole())
				.findFirst()
				.orElseThrow(IllegalArgumentException::new);
	}

	public static Role findById(Long id){
		return Arrays.stream(Role.values())
				.filter(obj -> obj.getId().equals(id))
				.findFirst()
				.orElseThrow(IllegalArgumentException::new);
	}

	public boolean isSystemRole() {
		return this.roleType == RoleType.SYSTEM;
	}

	public boolean isOrganizationRole() {
		return this.roleType == RoleType.ORGANIZATION;
	}

	public static List<Role> getSystemRoles() {
		return Arrays.stream(Role.values())
				.filter(role -> role.getRoleType() == RoleType.SYSTEM)
				.toList();
	}

	public static List<Role> getOrganizationRoles() {
		return Arrays.stream(Role.values())
				.filter(role -> role.getRoleType() == RoleType.ORGANIZATION)
				.toList();
	}
}
