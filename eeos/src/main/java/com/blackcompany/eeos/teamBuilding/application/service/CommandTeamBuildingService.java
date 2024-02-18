package com.blackcompany.eeos.teamBuilding.application.service;

import com.blackcompany.eeos.target.application.service.SelectTeamBuildingTargetService;
import com.blackcompany.eeos.teamBuilding.application.dto.CreateTeamBuildingRequest;
import com.blackcompany.eeos.teamBuilding.application.exception.NotFoundProgressTeamBuildingException;
import com.blackcompany.eeos.teamBuilding.application.model.RestrictTeamBuilding;
import com.blackcompany.eeos.teamBuilding.application.model.TeamBuildingModel;
import com.blackcompany.eeos.teamBuilding.application.model.converter.TeamBuildingEntityConverter;
import com.blackcompany.eeos.teamBuilding.application.model.converter.TeamBuildingRequestConverter;
import com.blackcompany.eeos.teamBuilding.application.usecase.CreateTeamBuildingUsecase;
import com.blackcompany.eeos.teamBuilding.application.usecase.EndTeamBuildingUsecase;
import com.blackcompany.eeos.teamBuilding.persistence.TeamBuildingEntity;
import com.blackcompany.eeos.teamBuilding.persistence.TeamBuildingRepository;
import com.blackcompany.eeos.teamBuilding.persistence.TeamBuildingStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommandTeamBuildingService
		implements CreateTeamBuildingUsecase, EndTeamBuildingUsecase {
	private static final RestrictTeamBuilding RESTRICT_TEAM_BUILDING = RestrictTeamBuilding.of();

	private final TeamBuildingRequestConverter requestConverter;
	private final TeamBuildingEntityConverter entityConverter;
	private final TeamBuildingRepository teamBuildingRepository;
	private final SelectTeamBuildingTargetService teamBuildingTargetService;

	@Override
	@Transactional
	public void create(Long memberId, CreateTeamBuildingRequest request) {
		RESTRICT_TEAM_BUILDING.addActiveCount();

		TeamBuildingModel model = requestConverter.from(memberId, request);
		TeamBuildingEntity savedEntity = teamBuildingRepository.save(entityConverter.toEntity(model));

		teamBuildingTargetService.save(savedEntity.getId(), request.getMembers());
	}

	@Override
	@Transactional
	public void delete(Long memberId) {
		TeamBuildingModel model =
				teamBuildingRepository
						.findByStatus(TeamBuildingStatus.PROGRESS)
						.map(entityConverter::from)
						.orElseThrow(NotFoundProgressTeamBuildingException::new);
		model.validateEdit(memberId);

		teamBuildingRepository.delete(entityConverter.toEntity(model));

		RESTRICT_TEAM_BUILDING.subtractActiveCount();
	}
}
