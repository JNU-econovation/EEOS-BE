package com.blackcompany.eeos.teamBuilding.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamBuildingResultRepository
		extends JpaRepository<TeamBuildingResultEntity, Long> {
	List<TeamBuildingResultEntity> findAllByTeamBuildingId(Long id);
}
