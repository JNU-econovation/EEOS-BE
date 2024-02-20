package com.blackcompany.eeos.teamBuilding.application.support;

import com.blackcompany.eeos.teamBuilding.application.model.Accessibility;
import com.blackcompany.eeos.teamBuilding.application.model.AccessibilityStandard;
import com.blackcompany.eeos.teamBuilding.application.service.ValidateService;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class ValidateTeamBuildingServiceComposite {
	private final Map<AccessibilityStandard, ValidateService> services;

	public ValidateTeamBuildingServiceComposite(Set<ValidateService> validateServices) {
		this.services =
				validateServices.stream()
						.collect(Collectors.toMap(ValidateService::support, Function.identity()));
	}

	public Accessibility getAccessibility(AccessibilityStandard standard, Long memberId) {
		ValidateService validateService = getValidateService(standard);

		return validateService.getAccessibility(memberId);
	}

	private ValidateService getValidateService(AccessibilityStandard standard) {
		return services.get(standard);
	}
}
