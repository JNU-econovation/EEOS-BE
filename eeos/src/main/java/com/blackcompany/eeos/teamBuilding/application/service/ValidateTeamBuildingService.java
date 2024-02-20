package com.blackcompany.eeos.teamBuilding.application.service;

import com.blackcompany.eeos.teamBuilding.application.dto.ValidateTeamBuildingResponse;
import com.blackcompany.eeos.teamBuilding.application.dto.converter.ValidateTeamBuildingResponseConverter;
import com.blackcompany.eeos.teamBuilding.application.model.Accessibility;
import com.blackcompany.eeos.teamBuilding.application.model.AccessibilityStandard;
import com.blackcompany.eeos.teamBuilding.application.support.ValidateTeamBuildingServiceComposite;
import com.blackcompany.eeos.teamBuilding.application.usecase.ValidateTeamBuildingUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ValidateTeamBuildingService implements ValidateTeamBuildingUsecase {
	private final ValidateTeamBuildingServiceComposite validateTeamBuildingServiceComposite;
	private final ValidateTeamBuildingResponseConverter responseConverter;

	@Override
	public ValidateTeamBuildingResponse validate(Long memberId, String status) {
		Accessibility accessibility =
				validateTeamBuildingServiceComposite.getAccessibility(
						AccessibilityStandard.find(status), memberId);

		return responseConverter.from(accessibility.getStatus());
	}
}
