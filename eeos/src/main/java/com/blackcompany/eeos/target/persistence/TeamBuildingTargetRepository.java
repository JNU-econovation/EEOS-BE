package com.blackcompany.eeos.target.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamBuildingTargetRepository
		extends JpaRepository<TeamBuildingTargetEntity, Long> {
	Optional<TeamBuildingTargetEntity> findByTeamBuildingIdAndMemberId(
			Long teamBuildingId, Long memberId);
}
