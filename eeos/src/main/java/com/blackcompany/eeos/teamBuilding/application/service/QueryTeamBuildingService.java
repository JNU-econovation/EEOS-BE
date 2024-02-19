package com.blackcompany.eeos.teamBuilding.application.service;

import com.blackcompany.eeos.teamBuilding.application.exception.NotFoundTeamBuildingStatusException;
import com.blackcompany.eeos.teamBuilding.persistence.TeamBuildingEntity;
import com.blackcompany.eeos.teamBuilding.persistence.TeamBuildingRepository;
import com.blackcompany.eeos.teamBuilding.persistence.TeamBuildingStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QueryTeamBuildingService {
	private final TeamBuildingRepository teamBuildingRepository;

	public TeamBuildingEntity getByStatus(TeamBuildingStatus status) {
		return teamBuildingRepository
				.findByStatus(status)
				.orElseThrow(() -> new NotFoundTeamBuildingStatusException(status.getStatus()));
	}
}
