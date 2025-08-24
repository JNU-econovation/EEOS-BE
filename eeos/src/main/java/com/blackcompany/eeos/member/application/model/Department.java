package com.blackcompany.eeos.member.application.model;

import java.util.Arrays;
import java.util.List;
import lombok.Getter;

@Getter
public enum Department {
	PRESIDENT(101L, "PRESIDENT", "회장단"),
	MARKETING(102L, "MARKETING", "홍보부"),
	MANAGEMENT(103L, "MANAGEMENT", "관리부"),
	EVENT(104L, "EVENT", "행사부"),
	NONE(105L, "NONE", "해당없음");

	private final Long id;
	private final String name;
	private final String koName;

	Department(Long id, String name, String koName) {
		this.id = id;
		this.name = name;
		this.koName = koName;
	}

	public static boolean isExist(String name){
		return Arrays.stream(Department.values())
				.anyMatch(obj -> obj.getName().equals(name));
	}

	public static Department findDepartment(String name){
		return Arrays.stream(Department.values())
				.filter(obj -> obj.getName().equals(name))
				.findFirst()
				.orElseThrow(IllegalArgumentException::new);
	}

	public static Department findById(Long id){
		return Arrays.stream(Department.values())
				.filter(obj -> obj.getId().equals(id))
				.findFirst()
				.orElseThrow(IllegalArgumentException::new);
	}

	public static List<Department> getAllDepartments() {
		return Arrays.asList(Department.values());
	}
}