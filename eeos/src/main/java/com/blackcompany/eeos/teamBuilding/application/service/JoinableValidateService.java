package com.blackcompany.eeos.teamBuilding.application.service;

import com.blackcompany.eeos.target.application.exception.NotFoundTargetTeamBuildingException;
import com.blackcompany.eeos.target.application.service.QueryTeamBuildingTargetService;
import com.blackcompany.eeos.teamBuilding.application.exception.EndTeamBuildingException;
import com.blackcompany.eeos.teamBuilding.application.model.Accessibility;
import com.blackcompany.eeos.teamBuilding.application.model.AccessibilityStandard;
import com.blackcompany.eeos.teamBuilding.application.model.TeamBuildingModel;
import com.blackcompany.eeos.teamBuilding.application.model.converter.TeamBuildingEntityConverter;
import com.blackcompany.eeos.teamBuilding.persistence.TeamBuildingRepository;
import com.blackcompany.eeos.teamBuilding.persistence.TeamBuildingStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class JoinableValidateService implements ValidateService {
	private final QueryTeamBuildingTargetService queryTeamBuildingTargetService;
	private final TeamBuildingRepository teamBuildingRepository;
	private final TeamBuildingEntityConverter entityConverter;

	@Override
	public AccessibilityStandard support() {
		return AccessibilityStandard.JOINABILITY;
	}

	@Override
	public Accessibility getAccessibility(Long memberId) {
		try {
			TeamBuildingModel model = findProgressTeamBuilding();
			queryTeamBuildingTargetService.getTarget(memberId, model.getId());
			return Accessibility.JOINABLE;
		} catch (EndTeamBuildingException | NotFoundTargetTeamBuildingException ex) {
			return Accessibility.NONJOINABLE;
		}
	}

	private TeamBuildingModel findProgressTeamBuilding() {
		return teamBuildingRepository
				.findByStatus(TeamBuildingStatus.PROGRESS)
				.map(entityConverter::from)
				.orElseGet(this::findCompleteTeamBuilding);
	}

	private TeamBuildingModel findCompleteTeamBuilding() {
		return teamBuildingRepository
				.findByStatus(TeamBuildingStatus.COMPLETE)
				.map(entityConverter::from)
				.orElseThrow(EndTeamBuildingException::new);
	}
}
