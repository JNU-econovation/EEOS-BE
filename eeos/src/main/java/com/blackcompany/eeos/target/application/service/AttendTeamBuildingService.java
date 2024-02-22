package com.blackcompany.eeos.target.application.service;

import com.blackcompany.eeos.member.application.service.QueryMemberService;
import com.blackcompany.eeos.target.application.dto.AttendTeamBuildingRequest;
import com.blackcompany.eeos.target.application.dto.QueryTargetInfoResponse;
import com.blackcompany.eeos.target.application.model.TeamBuildingTargetModel;
import com.blackcompany.eeos.target.application.model.converter.TeamBuildingTargetEntityConverter;
import com.blackcompany.eeos.target.application.usecase.AttendTeamBuildingUsecase;
import com.blackcompany.eeos.target.application.usecase.GetTargetInfoUsecase;
import com.blackcompany.eeos.target.application.usecase.UpdateAttendTeamBuildingUsecase;
import com.blackcompany.eeos.target.persistence.TeamBuildingTargetEntity;
import com.blackcompany.eeos.target.persistence.TeamBuildingTargetRepository;
import com.blackcompany.eeos.teamBuilding.application.model.TeamBuildingModel;
import com.blackcompany.eeos.teamBuilding.application.model.converter.TeamBuildingEntityConverter;
import com.blackcompany.eeos.teamBuilding.application.service.QueryTeamBuildingService;
import com.blackcompany.eeos.teamBuilding.persistence.TeamBuildingEntity;
import com.blackcompany.eeos.teamBuilding.persistence.TeamBuildingStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AttendTeamBuildingService
		implements AttendTeamBuildingUsecase, UpdateAttendTeamBuildingUsecase, GetTargetInfoUsecase {
	private final QueryTeamBuildingService queryTeamBuildingService;
	private final QueryTeamBuildingTargetService queryTeamBuildingTargetService;
	private final TeamBuildingTargetEntityConverter entityConverter;
	private final TeamBuildingTargetRepository teamBuildingTargetRepository;
	private final TeamBuildingEntityConverter teamBuildingEntityConverter;
	private final QueryMemberService queryMemberService;

	@Override
	@Transactional
	public void create(Long memberId, AttendTeamBuildingRequest request) {
		TeamBuildingTargetModel model = getTargetByActiveBuilding(memberId);

		TeamBuildingTargetEntity entity =
				entityConverter.toEntity(model.inputContent(request.getContent()));
		teamBuildingTargetRepository.save(entity);
	}

	@Override
	@Transactional
	public void update(Long memberId, AttendTeamBuildingRequest request) {
		validateAttendTeamBuilding();

		TeamBuildingTargetModel model = getTargetByActiveBuilding(memberId);
		TeamBuildingTargetEntity entity =
				entityConverter.toEntity(model.inputContent(request.getContent()));

		teamBuildingTargetRepository.save(entity);
	}

	@Override
	public QueryTargetInfoResponse getTargetInfo(Long memberId) {
		TeamBuildingTargetModel model = getTargetByActiveBuilding(memberId);
		String name = queryMemberService.getName(memberId);

		return QueryTargetInfoResponse.builder()
				.name(name)
				.status(model.getInputStatus())
				.content(model.getContent())
				.build();
	}

	private TeamBuildingTargetModel getTargetByActiveBuilding(Long memberId) {
		Long teamBuildingId = find(TeamBuildingStatus.PROGRESS).getId();

		return queryTeamBuildingTargetService.getTarget(memberId, teamBuildingId);
	}

	private void validateAttendTeamBuilding() {
		TeamBuildingModel teamBuildingModel = find(TeamBuildingStatus.PROGRESS);
		teamBuildingModel.validateAttend();
	}

	private TeamBuildingModel find(TeamBuildingStatus status) {
		TeamBuildingEntity entity = queryTeamBuildingService.getByStatus(status);
		return teamBuildingEntityConverter.from(entity);
	}
}
