package com.blackcompany.eeos.teamBuilding.application.service;

import com.blackcompany.eeos.teamBuilding.application.exception.DeniedOverUpperLimitException;
import com.blackcompany.eeos.teamBuilding.application.model.RestrictTeamBuildingModel;
import com.blackcompany.eeos.teamBuilding.application.model.converter.RestrictTeamBuildingConverter;
import com.blackcompany.eeos.teamBuilding.persistence.RestrictTeamBuildingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RestrictTeamBuildingService {
	private final RestrictTeamBuildingRepository restrictTeamBuildingRepository;
	private final RestrictTeamBuildingConverter restrictTeamBuildingConverter;
	private final QueryRestrictTeamBuildingService queryRestrictTeamBuildingService;

	@Transactional
	public void addTeamBuilding() {
		RestrictTeamBuildingModel model = getRestrict().addActiveCount();
		try {
			restrictTeamBuildingRepository.save(restrictTeamBuildingConverter.toEntity(model));
		} catch (OptimisticLockingFailureException ex) {
			throw new DeniedOverUpperLimitException();
		}
	}

	@Transactional
	public void subtractTeamBuilding() {
		RestrictTeamBuildingModel model = getRestrict().subtractActiveCount();
		restrictTeamBuildingRepository.save(restrictTeamBuildingConverter.toEntity(model));
	}

	private RestrictTeamBuildingModel getRestrict() {
		return queryRestrictTeamBuildingService.getRestrict();
	}
}
