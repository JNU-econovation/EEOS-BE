package com.blackcompany.eeos.teamBuilding.application.service;

import com.blackcompany.eeos.attend.application.service.SelectTeamBuildingTargetService;
import com.blackcompany.eeos.teamBuilding.application.dto.CreateTeamBuildingRequest;
import com.blackcompany.eeos.teamBuilding.application.model.TeamBuildingModel;
import com.blackcompany.eeos.teamBuilding.application.model.converter.TeamBuildingEntityConverter;
import com.blackcompany.eeos.teamBuilding.application.model.converter.TeamBuildingRequestConverter;
import com.blackcompany.eeos.teamBuilding.application.usecase.CreateTeamBuildingUsecase;
import com.blackcompany.eeos.teamBuilding.persistence.TeamBuildingEntity;
import com.blackcompany.eeos.teamBuilding.persistence.TeamBuildingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommandTeamBuildingService implements CreateTeamBuildingUsecase {
	private final TeamBuildingRequestConverter requestConverter;
	private final TeamBuildingEntityConverter entityConverter;
	private final TeamBuildingRepository repository;
	private final SelectTeamBuildingTargetService teamBuildingTargetService;

	@Override
	@Transactional
	public void create(Long memberId, CreateTeamBuildingRequest request) {
		TeamBuildingModel model = requestConverter.from(request);
		TeamBuildingEntity savedEntity = repository.save(entityConverter.toEntity(model));

		teamBuildingTargetService.save(savedEntity.getId(), request.getMembers());
	}
}
