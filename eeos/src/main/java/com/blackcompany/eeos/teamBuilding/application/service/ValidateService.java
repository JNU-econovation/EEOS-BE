package com.blackcompany.eeos.teamBuilding.application.service;

import com.blackcompany.eeos.teamBuilding.application.model.Accessibility;
import com.blackcompany.eeos.teamBuilding.application.model.AccessibilityStandard;

public interface ValidateService {
	AccessibilityStandard support();

	Accessibility getAccessibility(Long memberId);
}
