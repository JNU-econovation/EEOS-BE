package com.blackcompany.eeos.team.application.Service;

import com.blackcompany.eeos.member.application.service.QueryMemberService;
import com.blackcompany.eeos.target.persistence.PresentationRepository;
import com.blackcompany.eeos.team.application.dto.CreateTeamRequest;
import com.blackcompany.eeos.team.application.dto.CreateTeamResponse;
import com.blackcompany.eeos.team.application.dto.QueryTeamsResponse;
import com.blackcompany.eeos.team.application.dto.converter.CreateTeamRequestConverter;
import com.blackcompany.eeos.team.application.dto.converter.QueryTeamResponseConverter;
import com.blackcompany.eeos.team.application.dto.converter.TeamResponseConverter;
import com.blackcompany.eeos.team.application.event.DeletedTeamEvent;
import com.blackcompany.eeos.team.application.exception.DeniedTeamEditException;
import com.blackcompany.eeos.team.application.exception.DuplicateTeamNameException;
import com.blackcompany.eeos.team.application.exception.NotFoundTeamException;
import com.blackcompany.eeos.team.application.exception.NotFoundTeamStatusException;
import com.blackcompany.eeos.team.application.model.TeamModel;
import com.blackcompany.eeos.team.application.model.converter.TeamEntityConverter;
import com.blackcompany.eeos.team.application.usecase.CreateTeamUsecase;
import com.blackcompany.eeos.team.application.usecase.DeleteTeamUsecase;
import com.blackcompany.eeos.team.application.usecase.GetTeamsByActiveStatus;
import com.blackcompany.eeos.team.persistence.TeamEntity;
import com.blackcompany.eeos.team.persistence.TeamRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class TeamService implements CreateTeamUsecase, DeleteTeamUsecase, GetTeamsByActiveStatus {

	private final CreateTeamRequestConverter createTeamRequestConverter;
	private final TeamEntityConverter entityConverter;
	private final TeamRepository teamRepository;
	private final TeamResponseConverter teamResponseConverter;
	private final ApplicationEventPublisher applicationEventPublisher;
	private final QueryTeamResponseConverter queryTeamResponseConverter;
	private final QueryMemberService memberService;
	private final PresentationRepository presentationRepository;

	@Override
	@Transactional
	public CreateTeamResponse create(final Long memberId, final CreateTeamRequest request) {
		validateUser(memberId);
		TeamModel model = createTeamRequestConverter.from(request);
		Long saveId = createTeam(model);

		return teamResponseConverter.from(saveId);
	}

	@Override
	@Transactional
	public void delete(final Long memberId, final Long teamId) {
		TeamModel team = findTeam(teamId);
		validateUser(memberId);

		teamRepository.deleteTeamEntityByName(team.getId());
		applicationEventPublisher.publishEvent(DeletedTeamEvent.of(teamId));
	}

	@Override
	public QueryTeamsResponse execute(final String programId) {
		Long numberFormatId;
		List<TeamModel> models;
		if ("none".equals(programId)) {
			models = findTeams();
			return queryTeamResponseConverter.from(models);
			// 이 방법 외에 programId로 들어오는 none, 숫자, 올바르지 않은 형식 이 3가지를 구별할 수 있는 방법이 있을까??
		} else {
			try {
				numberFormatId = Long.parseLong(programId);
				// 유효한 숫자인 경우, 숫자 programId에 대한 특정 로직을 처리
			} catch (NumberFormatException e) {
				// programId가 "none"도 아니고 유효한 숫자도 아닌 경우, 잘못된 형식으로 처리
				throw new NotFoundTeamStatusException(programId);
			}

			models = findTeamByProgram(numberFormatId);
		}

		return queryTeamResponseConverter.from(models);
	}

	private List<TeamModel> findTeamByProgram(Long programId) {
		return presentationRepository.findTeamsByProgramId(programId).stream()
				.map(entityConverter::from)
				.collect(Collectors.toList());
	}

	private List<TeamModel> findTeams() {
		return teamRepository.findAllTeamsByStatus().stream()
				.map(entityConverter::from)
				.collect(Collectors.toList());
	}

	private void validateUser(Long memberId) {
		if (!memberService.findMember(memberId).isAdmin()) throw new DeniedTeamEditException(memberId);
	}

	private Long createTeam(TeamModel model) {
		TeamEntity database = findTeamName(model.getName()).orElse(null);
		validateCreateTeam(database); // 팀명존재, 활동상태 true -> 중복

		if (database == null) { // 팀명이 존재하지 않는 상황일 때
			TeamEntity entity = entityConverter.toEntity(model); // 모델 or entity
			TeamEntity save = teamRepository.save(entity);
			return save.getId();
		} else {
			return updateTeamStatus(database); // 팀명은 존재하지만, 활동상태가 false 일 때
		}
	}

	private Long updateTeamStatus(TeamEntity teamEntity) {
		teamEntity.updateTeamStatus(true);
		return teamEntity.getId();
	}

	private void validateCreateTeam(TeamEntity database) {
		if (database != null && database.isStatus()) {
			throw new DuplicateTeamNameException(database.getName());
		}
	}

	private Optional<TeamEntity> findTeamName(String teamName) {
		return teamRepository.findTeamEntityByName(teamName).stream().findFirst();
	}

	private TeamModel findTeam(final Long teamId) {
		return teamRepository
				.findById(teamId)
				.map(entityConverter::from)
				.orElseThrow( NotFoundTeamException::new);
	}
}
