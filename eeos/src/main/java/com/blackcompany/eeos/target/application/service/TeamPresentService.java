package com.blackcompany.eeos.target.application.service;

import com.blackcompany.eeos.target.application.usecase.PresentTeamUsecase;
import com.blackcompany.eeos.target.persistence.PresentationEntity;
import com.blackcompany.eeos.target.persistence.PresentationRepository;
import com.blackcompany.eeos.target.persistence.converter.PresentationConverter;
import java.util.List;
import java.util.stream.Collectors;

import com.blackcompany.eeos.team.application.exception.NotFoundTeamException;
import com.blackcompany.eeos.team.application.model.TeamModel;
import com.blackcompany.eeos.team.application.model.converter.TeamEntityConverter;
import com.blackcompany.eeos.team.persistence.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamPresentService implements PresentTeamUsecase {

	private final PresentationRepository presentationRepository;
	private final PresentationConverter presentationConverter;
	private final TeamRepository teamRepository;
	private final TeamEntityConverter teamEntityConverter;

	@Override
	public void save(Long programId, List<Long> teamIds) {
		validateTeams(teamIds);

		List<PresentationEntity> entities = toEntities(programId, teamIds);

		entities.stream().map(presentationRepository::save);
	}

	private void validateTeams(List<Long> teamIds) {
		if(isExistTeams(teamIds)){
			throw new NotFoundTeamException();
		}
	}

	private List<PresentationEntity> toEntities(Long programId, List<Long> teamIds) {
		return teamIds.stream()
				.map(id -> presentationConverter.from(programId, id))
				.collect(Collectors.toList());
	}

	private boolean isExistTeams(List<Long> teamIds){
		long originSize = teamIds.size();
		long filteredSize = findAllTeam().stream()
				.map(TeamModel::getId)
				.filter(teamIds::contains)
				.count();
		return originSize==filteredSize;
	}

	private List<TeamModel> findAllTeam(){
		return teamRepository.findAllTeamsByStatus().stream()
				.map(teamEntityConverter::from)
				.collect(Collectors.toList());
	}
}
