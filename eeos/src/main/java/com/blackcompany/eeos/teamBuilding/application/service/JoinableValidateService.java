package com.blackcompany.eeos.teamBuilding.application.service;

import com.blackcompany.eeos.target.application.exception.NotFoundTargetTeamBuildingException;
import com.blackcompany.eeos.target.application.service.QueryTeamBuildingTargetService;
import com.blackcompany.eeos.teamBuilding.application.model.Accessibility;
import com.blackcompany.eeos.teamBuilding.application.model.AccessibilityStandard;
import com.blackcompany.eeos.teamBuilding.persistence.TeamBuildingEntity;
import com.blackcompany.eeos.teamBuilding.persistence.TeamBuildingStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class JoinableValidateService implements ValidateService {
	private final QueryTeamBuildingService queryTeamBuildingService;
	private final QueryTeamBuildingTargetService queryTeamBuildingTargetService;

	@Override
	public AccessibilityStandard support() {
		return AccessibilityStandard.JOINABILITY;
	}

	@Override
	public Accessibility getAccessibility(Long memberId) {
		TeamBuildingEntity entity = queryTeamBuildingService.getByStatus(TeamBuildingStatus.PROGRESS);
		try {
			queryTeamBuildingTargetService.getTarget(memberId, entity.getId());
			return Accessibility.JOINABLE;
		} catch (NotFoundTargetTeamBuildingException ex) {
			return Accessibility.NONCREATABLE;
		}
	}
}
