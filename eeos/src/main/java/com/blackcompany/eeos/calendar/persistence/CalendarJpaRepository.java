package com.blackcompany.eeos.calendar.persistence;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CalendarJpaRepository extends JpaRepository<CalendarEntity, Long> {

    @Query("SELECT c FROM CalendarEntity c WHERE c.startAt between :startAt and :endAt")
    List<CalendarEntity> findBetween(@Param("startAt") LocalDateTime startAt, @Param("endAt")  LocalDateTime endAt);

}
