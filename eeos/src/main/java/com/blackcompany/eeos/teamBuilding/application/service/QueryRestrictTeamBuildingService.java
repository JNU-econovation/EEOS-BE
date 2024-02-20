package com.blackcompany.eeos.teamBuilding.application.service;

import com.blackcompany.eeos.teamBuilding.application.exception.NotFoundTeamBuildingStatusException;
import com.blackcompany.eeos.teamBuilding.application.model.RestrictTeamBuildingModel;
import com.blackcompany.eeos.teamBuilding.application.model.converter.RestrictTeamBuildingConverter;
import com.blackcompany.eeos.teamBuilding.persistence.RestrictTeamBuildingRepository;
import com.blackcompany.eeos.teamBuilding.persistence.TeamBuildingStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QueryRestrictTeamBuildingService {
	private final RestrictTeamBuildingRepository restrictTeamBuildingRepository;
	private final RestrictTeamBuildingConverter restrictTeamBuildingConverter;

	public RestrictTeamBuildingModel getRestrict() {
		return restrictTeamBuildingRepository
				.findTopByOrderByVersion()
				.map(restrictTeamBuildingConverter::from)
				.orElseThrow(
						() -> new NotFoundTeamBuildingStatusException(TeamBuildingStatus.PROGRESS.getStatus()));
	}
}
