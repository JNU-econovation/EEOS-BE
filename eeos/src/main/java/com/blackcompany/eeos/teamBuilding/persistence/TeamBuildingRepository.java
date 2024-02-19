package com.blackcompany.eeos.teamBuilding.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamBuildingRepository extends JpaRepository<TeamBuildingEntity, Long> {
	Optional<TeamBuildingEntity> findByStatus(TeamBuildingStatus status);
}
