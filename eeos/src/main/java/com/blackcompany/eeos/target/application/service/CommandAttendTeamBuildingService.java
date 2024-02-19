package com.blackcompany.eeos.target.application.service;

import com.blackcompany.eeos.target.application.dto.AttendTeamBuildingRequest;
import com.blackcompany.eeos.target.application.model.TeamBuildingTargetModel;
import com.blackcompany.eeos.target.application.model.converter.TeamBuildingTargetEntityConverter;
import com.blackcompany.eeos.target.application.usecase.AttendTeamBuildingUsecase;
import com.blackcompany.eeos.target.persistence.TeamBuildingTargetEntity;
import com.blackcompany.eeos.target.persistence.TeamBuildingTargetRepository;
import com.blackcompany.eeos.teamBuilding.application.service.QueryTeamBuildingService;
import com.blackcompany.eeos.teamBuilding.persistence.TeamBuildingStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommandAttendTeamBuildingService implements AttendTeamBuildingUsecase {
	private final QueryTeamBuildingService queryTeamBuildingService;
	private final QueryTeamBuildingTargetService queryTeamBuildingTargetService;
	private final TeamBuildingTargetEntityConverter entityConverter;
	private final TeamBuildingTargetRepository teamBuildingTargetRepository;

	@Override
	@Transactional
	public void create(Long memberId, AttendTeamBuildingRequest request) {
		TeamBuildingTargetModel model = getTargetByActiveBuilding(memberId);

		TeamBuildingTargetEntity newEntity =
				entityConverter.toEntity(model.inputContent(request.getContent()));
		teamBuildingTargetRepository.save(newEntity);
	}

	private TeamBuildingTargetModel getTargetByActiveBuilding(Long memberId) {
		Long teamBuildingId = queryTeamBuildingService.getByStatus(TeamBuildingStatus.PROGRESS).getId();

		TeamBuildingTargetEntity entity =
				queryTeamBuildingTargetService.getTarget(memberId, teamBuildingId);
		return entityConverter.from(entity);
	}
}