package com.blackcompany.eeos.program.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCalendarRepository extends JpaRepository<CalendarEntity, Long> {
	Optional<CalendarEntity> findTopByOrderByCreatedDateDesc();
}
