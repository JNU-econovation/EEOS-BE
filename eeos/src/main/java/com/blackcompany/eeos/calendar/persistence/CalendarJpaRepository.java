package com.blackcompany.eeos.calendar.persistence;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CalendarJpaRepository extends JpaRepository<CalendarEntity, Long> {

	//	@Query("SELECT c FROM CalendarEntity c "
	//			+ "WHERE (c.startAt <= :startAt and c.endAt <= :endAt)" // (행사 시작) ~ 조회 시작 날짜 ~ (행사 종료) ~ 조회
	// 마지막 날짜인 경우
	//			+ "OR (c.startAt >= :startAt and c.endAt <= :endAt)" // 조회 시작 날짜 ~ (행사 시작 ~ 행사 종료) ~ 조회 마지막
	// 날짜인 경우
	//			+ "OR (c.startAt >= :startAt and c.endAt >= :endAt)" // 조회 시작 날짜 ~ (행사 시작) ~ 조회 마지막 날짜 ~ (행사
	// 종료)인 경우
	//			+ "OR (c.startAt <= :startAt and c.endAt >= :endAt)" // (행사 시작) ~ 조회 시작 날짜 ~ 조회 마지막 날짜 ~ (행사
	// 종료)인 경우
	//			+ "ORDER BY c.createdDate ASC")
	@Query(
			"SELECT c FROM CalendarEntity c "
					+ "WHERE (c.startAt <= :endAt and c.endAt >= :startAt)"
					+ "ORDER BY c.startAt ASC")
	List<CalendarEntity> findBetween(
			@Param("startAt") LocalDateTime startAt, @Param("endAt") LocalDateTime endAt);

	@Query("SELECT c FROM CalendarEntity c "
			+ "WHERE c.startAt BETWEEN :from AND :to "
			+ "ORDER BY c.startAt ASC")
	List<CalendarEntity> findNotStarted(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);
}
