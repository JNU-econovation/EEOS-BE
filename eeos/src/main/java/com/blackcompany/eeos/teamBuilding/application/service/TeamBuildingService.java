package com.blackcompany.eeos.teamBuilding.application.service;

import com.blackcompany.eeos.member.persistence.MemberEntity;
import com.blackcompany.eeos.member.persistence.MemberRepository;
import com.blackcompany.eeos.target.application.model.TeamBuildingTargetModel;
import com.blackcompany.eeos.target.application.service.QueryTeamBuildingTargetService;
import com.blackcompany.eeos.target.application.service.SelectTeamBuildingTargetService;
import com.blackcompany.eeos.teamBuilding.application.dto.CreateTeamBuildingRequest;
import com.blackcompany.eeos.teamBuilding.application.dto.EachMemberResponse;
import com.blackcompany.eeos.teamBuilding.application.dto.ResultTeamBuildingResponse;
import com.blackcompany.eeos.teamBuilding.application.dto.converter.ResultTeamBuildingResponseConverter;
import com.blackcompany.eeos.teamBuilding.application.exception.NotFoundTeamBuildingStatusException;
import com.blackcompany.eeos.teamBuilding.application.model.TeamBuildingAccessRights;
import com.blackcompany.eeos.teamBuilding.application.model.TeamBuildingModel;
import com.blackcompany.eeos.teamBuilding.application.model.converter.TeamBuildingEntityConverter;
import com.blackcompany.eeos.teamBuilding.application.model.converter.TeamBuildingRequestConverter;
import com.blackcompany.eeos.teamBuilding.application.model.converter.TeamBuildingResultConverter;
import com.blackcompany.eeos.teamBuilding.application.usecase.CompleteTeamBuildingUsecase;
import com.blackcompany.eeos.teamBuilding.application.usecase.CreateTeamBuildingUsecase;
import com.blackcompany.eeos.teamBuilding.application.usecase.EndTeamBuildingUsecase;
import com.blackcompany.eeos.teamBuilding.application.usecase.GetResultTeamBuildingUsecase;
import com.blackcompany.eeos.teamBuilding.application.usecase.GetTeamBuildingUsecase;
import com.blackcompany.eeos.teamBuilding.application.usecase.QueryTeamBuildingResponse;
import com.blackcompany.eeos.teamBuilding.infra.client.ClusteringTeamApiClient;
import com.blackcompany.eeos.teamBuilding.infra.dto.converter.ParticipantRequestConverter;
import com.blackcompany.eeos.teamBuilding.persistence.TeamBuildingEntity;
import com.blackcompany.eeos.teamBuilding.persistence.TeamBuildingRepository;
import com.blackcompany.eeos.teamBuilding.persistence.TeamBuildingResultEntity;
import com.blackcompany.eeos.teamBuilding.persistence.TeamBuildingResultRepository;
import com.blackcompany.eeos.teamBuilding.persistence.TeamBuildingStatus;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamBuildingService
		implements CreateTeamBuildingUsecase,
				EndTeamBuildingUsecase,
				CompleteTeamBuildingUsecase,
				GetResultTeamBuildingUsecase,
				GetTeamBuildingUsecase {
	private final RestrictTeamBuildingService restrictTeamBuildingService;
	private final TeamBuildingRequestConverter requestConverter;
	private final TeamBuildingEntityConverter entityConverter;
	private final TeamBuildingRepository teamBuildingRepository;
	private final SelectTeamBuildingTargetService teamBuildingTargetService;
	private final QueryTeamBuildingService queryTeamBuildingService;
	private final QueryTeamBuildingTargetService queryTeamBuildingTargetService;
	private final ClusteringTeamApiClient clusteringTeamApiClient;
	private final ParticipantRequestConverter apiClientRequestConverter;
	private final TeamBuildingResultRepository teamBuildingResultRepository;
	private final TeamBuildingResultConverter teamBuildingResultConverter;
	private final MemberRepository memberRepository;
	private final ResultTeamBuildingResponseConverter responseConverter;

	@Override
	@Transactional
	public void create(Long memberId, CreateTeamBuildingRequest request) {
		restrictTeamBuildingService.addTeamBuilding();

		TeamBuildingModel model = requestConverter.from(memberId, request, TeamBuildingStatus.PROGRESS);
		TeamBuildingEntity savedEntity = teamBuildingRepository.save(entityConverter.toEntity(model));

		teamBuildingTargetService.save(savedEntity.getId(), request.getMembers());
	}

	@Override
	@Transactional
	public void delete(Long memberId) {
		TeamBuildingStatus status = TeamBuildingStatus.COMPLETE;

		TeamBuildingModel model =
				teamBuildingRepository
						.findByStatus(status)
						.map(entityConverter::from)
						.orElseThrow(() -> new NotFoundTeamBuildingStatusException(status.getStatus()));
		model.validateEdit(memberId);

		teamBuildingRepository.delete(entityConverter.toEntity(model, status));

		restrictTeamBuildingService.subtractTeamBuilding();
	}

	@Override
	@Transactional
	public void complete(Long memberId) {
		TeamBuildingEntity entity = queryTeamBuildingService.getByStatus(TeamBuildingStatus.PROGRESS);
		TeamBuildingModel model = entityConverter.from(entity);

		requestResult(model);
		updateStatus(memberId);
	}

	@Override
	public ResultTeamBuildingResponse getResult(Long memberId) {
		Long teamBuildingId = queryTeamBuildingService.getByStatus(TeamBuildingStatus.COMPLETE).getId();

		List<List<Long>> memberIds =
				teamBuildingResultRepository.findAllByTeamBuildingId(teamBuildingId).stream()
						.map(TeamBuildingResultEntity::getMemberIds)
						.collect(Collectors.toList());

		List<List<MemberEntity>> members = getMembers(memberIds);

		TeamBuildingEntity entity = queryTeamBuildingService.getByStatus(TeamBuildingStatus.COMPLETE);
		TeamBuildingModel model = entityConverter.from(entity);

		return responseConverter.from(
				model.getAccessRight(memberId).getAccessRight(), combines(members, memberIds));
	}

	@Override
	public QueryTeamBuildingResponse getTeamBuilding(Long memberId, String teaBuildingStatus) {
		TeamBuildingStatus status = TeamBuildingStatus.find(teaBuildingStatus);

		TeamBuildingModel model =
				teamBuildingRepository
						.findByStatus(status)
						.map(entityConverter::from)
						.orElseThrow(() -> new NotFoundTeamBuildingStatusException(status.getStatus()));

		TeamBuildingAccessRights accessRight = model.getAccessRight(memberId);

		return QueryTeamBuildingResponse.builder()
				.title(model.getTitle())
				.content(model.getContent())
				.accessRight(accessRight.getAccessRight())
				.build();
	}

	private List<List<EachMemberResponse>> combines(
			List<List<MemberEntity>> members, List<List<Long>> memberIds) {
		return members.stream()
				.map(memberGroup -> combine(memberGroup, memberIds.get(members.indexOf(memberGroup))))
				.collect(Collectors.toList());
	}

	private List<EachMemberResponse> combine(List<MemberEntity> members, List<Long> memberIds) {
		return members.stream()
				.filter(member -> memberIds.contains(member.getId()))
				.map(member -> combine(member.getName(), member.getId()))
				.collect(Collectors.toList());
	}

	private EachMemberResponse combine(String name, Long memberId) {
		return EachMemberResponse.builder().memberId(memberId).name(name).build();
	}

	private void requestResult(TeamBuildingModel model) {
		List<TeamBuildingTargetModel> targetModels =
				queryTeamBuildingTargetService.getTarget(model.getId());

		List<List<String>> datas =
				clusteringTeamApiClient.fetchResult(
						apiClientRequestConverter.from(targetModels, model.getMaxTeamSize()));

		List<TeamBuildingResultEntity> results =
				datas.stream()
						.map(ids -> teamBuildingResultConverter.from(ids, model.getId()))
						.collect(Collectors.toList());

		teamBuildingResultRepository.saveAll(results);
	}

	private void updateStatus(Long memberId) {
		TeamBuildingEntity entity = queryTeamBuildingService.getByStatus(TeamBuildingStatus.PROGRESS);
		TeamBuildingModel model = entityConverter.from(entity);

		TeamBuildingModel updateStatus =
				model.updateStatus(TeamBuildingStatus.COMPLETE.getStatus(), memberId);

		teamBuildingRepository.save(entityConverter.toEntity(updateStatus));
	}

	private List<List<MemberEntity>> getMembers(List<List<Long>> memberIds) {
		return memberIds.stream().map(memberRepository::findMembersByIds).collect(Collectors.toList());
	}
}
