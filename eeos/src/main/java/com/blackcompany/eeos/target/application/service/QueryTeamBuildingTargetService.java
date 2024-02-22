package com.blackcompany.eeos.target.application.service;

import com.blackcompany.eeos.target.application.exception.NotFoundTargetTeamBuildingException;
import com.blackcompany.eeos.target.application.model.TeamBuildingTargetModel;
import com.blackcompany.eeos.target.application.model.converter.TeamBuildingTargetEntityConverter;
import com.blackcompany.eeos.target.persistence.TeamBuildingTargetRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QueryTeamBuildingTargetService {
	private final TeamBuildingTargetRepository teamBuildingTargetRepository;
	private final TeamBuildingTargetEntityConverter entityConverter;

	@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
	public TeamBuildingTargetModel getTarget(Long memberId, Long teamBuildingId) {
		return teamBuildingTargetRepository
				.findByTeamBuildingIdAndMemberId(teamBuildingId, memberId)
				.map(entityConverter::from)
				.orElseThrow(NotFoundTargetTeamBuildingException::new);
	}

	public List<TeamBuildingTargetModel> getTarget(Long teamBuildingId) {
		return teamBuildingTargetRepository.findByTeamBuildingId(teamBuildingId).stream()
				.map(entityConverter::from)
				.collect(Collectors.toList());
	}
}
