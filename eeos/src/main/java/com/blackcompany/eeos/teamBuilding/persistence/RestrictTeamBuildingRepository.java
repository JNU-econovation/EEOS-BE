package com.blackcompany.eeos.teamBuilding.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestrictTeamBuildingRepository
		extends JpaRepository<RestrictTeamBuildingEntity, Long> {
	Optional<RestrictTeamBuildingEntity> findTopByOrderByVersion();
}
