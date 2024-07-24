package com.blackcompany.eeos.target.application.service;

import com.blackcompany.eeos.target.application.usecase.PresentTeamUsecase;
import com.blackcompany.eeos.target.persistence.PresentationEntity;
import com.blackcompany.eeos.target.persistence.PresentationRepository;
import com.blackcompany.eeos.target.persistence.converter.PresentationConverter;
import com.blackcompany.eeos.team.application.exception.NotFoundTeamException;
import com.blackcompany.eeos.team.application.model.converter.TeamEntityConverter;
import com.blackcompany.eeos.team.persistence.TeamRepository;
import java.util.List;
import java.util.stream.Collectors;
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

		presentationRepository.saveAll(entities);
	}

	private void validateTeams(List<Long> teamIds) {
		teamIds.forEach(
				id -> {
					if (!teamRepository.existsById(id)) throw new NotFoundTeamException(id);
				});
	}

	private List<PresentationEntity> toEntities(Long programId, List<Long> teamIds) {
		return teamIds.stream()
				.map(id -> presentationConverter.from(programId, id))
				.collect(Collectors.toList());
	}
}
