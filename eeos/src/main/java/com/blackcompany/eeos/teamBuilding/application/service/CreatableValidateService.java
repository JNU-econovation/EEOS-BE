package com.blackcompany.eeos.teamBuilding.application.service;

import com.blackcompany.eeos.teamBuilding.application.model.Accessibility;
import com.blackcompany.eeos.teamBuilding.application.model.AccessibilityStandard;
import com.blackcompany.eeos.teamBuilding.application.model.RestrictTeamBuildingModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CreatableValidateService implements ValidateService {
	private final QueryRestrictTeamBuildingService queryRestrictTeamBuildingService;

	@Override
	public AccessibilityStandard support() {
		return AccessibilityStandard.CREATABILITY;
	}

	@Override
	public Accessibility getAccessibility(Long memberId) {
		RestrictTeamBuildingModel model = queryRestrictTeamBuildingService.getRestrict();

		return model.getCreatableAccessibility();
	}
}
