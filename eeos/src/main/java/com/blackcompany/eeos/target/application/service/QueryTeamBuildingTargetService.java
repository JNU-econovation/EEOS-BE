package com.blackcompany.eeos.target.application.service;

import com.blackcompany.eeos.target.application.exception.NotFoundTargetTeamBuildingException;
import com.blackcompany.eeos.target.persistence.TeamBuildingTargetEntity;
import com.blackcompany.eeos.target.persistence.TeamBuildingTargetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QueryTeamBuildingTargetService {
	private final TeamBuildingTargetRepository teamBuildingTargetRepository;

	public TeamBuildingTargetEntity getTarget(Long memberId, Long teamBuildingId) {
		return teamBuildingTargetRepository
				.findByTeamBuildingIdAndMemberId(teamBuildingId, memberId)
				.orElseThrow(NotFoundTargetTeamBuildingException::new);
	}
}
