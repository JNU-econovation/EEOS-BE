package com.blackcompany.eeos.program.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaSemesterPeriodRepository extends JpaRepository<SemesterPeriodEntity, Long> {
	Optional<SemesterPeriodEntity> findTopByOrderByCreatedDateDesc();
}
