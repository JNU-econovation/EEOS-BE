package com.blackcompany.eeos.teamBuilding.application.model;

import java.util.Arrays;
import java.util.List;

public enum AccessibilityStandard {
	CREATABILITY("creatability", List.of(Accessibility.CREATABLE, Accessibility.NONCREATABLE)),
	JOINABILITY("joinability", List.of(Accessibility.JOINABLE, Accessibility.NONJOINABLE));

	private final String standard;
	private final List<Accessibility> accessibilities;

	AccessibilityStandard(String standard, List<Accessibility> accessibilities) {
		this.standard = standard;
		this.accessibilities = accessibilities;
	}

	public String getStandard() {
		return standard;
	}

	public List<Accessibility> getAccessibilities() {
		return accessibilities;
	}

	public static AccessibilityStandard find(String source) {
		return Arrays.stream(AccessibilityStandard.values())
				.filter(standard -> standard.getStandard().equals(source))
				.findAny()
				.orElse(AccessibilityStandard.CREATABILITY);
	}
}
