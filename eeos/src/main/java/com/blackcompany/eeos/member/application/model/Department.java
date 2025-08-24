package com.blackcompany.eeos.member.application.model;

import java.util.Arrays;
import java.util.List;
import lombok.Getter;

@Getter
public enum Department {
	PRESIDENT(101L, "PRESIDENT"),
	MARKETING(102L, "MARKETING"),
	MANAGEMENT(103L, "MANAGEMENT"),
	EVENT(104L, "EVENT"),
	NONE(105L, "NONE");

	private final Long id;
	private final String name;

	Department(Long id, String name) {
		this.id = id;
		this.name = name;
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