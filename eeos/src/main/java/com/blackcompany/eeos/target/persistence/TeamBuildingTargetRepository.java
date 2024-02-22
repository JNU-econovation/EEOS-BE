package com.blackcompany.eeos.target.persistence;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TeamBuildingTargetRepository
		extends JpaRepository<TeamBuildingTargetEntity, Long> {
	Optional<TeamBuildingTargetEntity> findByTeamBuildingIdAndMemberId(
			Long teamBuildingId, Long memberId);

	List<TeamBuildingTargetEntity> findByTeamBuildingId(Long teamBuildingId);

	@Query("DELETE FROM TeamBuildingTargetEntity t WHERE t.teamBuildingId = :teamBuildingId")
	void deleteByTeamBuildingId(@Param("teamBuildingId") Long teamBuildingId);
}
