package com.blackcompany.eeos.target.persistence;

import com.blackcompany.eeos.target.application.model.AttendStatus;
import java.sql.Timestamp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PenaltyPointRepository extends JpaRepository<AttendEntity, Long> {

	@Query(
			"SELECT COUNT(*) FROM (SELECT a.memberId as memberId, a.penaltyScore as penaltyScore FROM AttendEntity a WHERE a.createdDate >= :startDate AND a.createdDate <= :endDate) AS temp WHERE temp.penaltyScore = :penaltyPoint")
	Long countByPenaltyPoint(
			@Param("startDate") Timestamp startDate,
			@Param("endDate") Timestamp endDate,
			@Param("penaltyPoint") Long penaltyPoint);

	@Query(
			"SELECT COUNT(*) FROM (SELECT a.memberId as memberId, a.penaltyScore as penaltyScore FROM AttendEntity a WHERE a.createdDate >= :startDate AND a.createdDate <= :endDate) AS temp WHERE temp.penaltyScore > :penaltyPoint")
	Long countByPenaltyPointGreaterThan(
			@Param("startDate") Timestamp startDate,
			@Param("endDate") Timestamp endDate,
			@Param("penaltyPoint") Long penaltyPoint);

	@Query(
			"SELECT SUM(a.penaltyScore) FROM AttendEntity a WHERE a.createdDate >= :startDate AND a.createdDate <= :endDate AND a.memberId=:memberId")
	Long findTotalPenaltyScoreByMemberId(
			@Param("startDate") Timestamp startDate,
			@Param("endDate") Timestamp endDate,
			@Param("memberId") Long memberId);

	@Query(
			"SELECT temp.memberId, temp.totalScore FROM (SELECT a.memberId as memberId, SUM(a.penaltyScore) as totalScore FROM AttendEntity a WHERE a.createdDate >= :startDate AND a.createdDate <= :endDate  GROUP BY a.memberId) AS temp")
	Page<Object[]> findByPenaltyPointSum(
			@Param("startDate") Timestamp startDate,
			@Param("endDate") Timestamp endDate,
			Pageable pageable);

	@Query(
			"SELECT temp.memberId, temp.totalScore FROM (SELECT a.memberId as memberId, SUM(a.penaltyScore) as totalScore FROM AttendEntity a GROUP BY a.memberId) AS temp")
	Page<Object[]> findByPenaltyPointSum(Pageable pageable);

	@Query("SELECT"
			+ "  m.id,"
			+ "  m.name,"
			+ "  m.activeStatus,"
			+ "  SUM(CASE WHEN a.status = :late THEN 1 ELSE 0 END) as late,"
			+ "  SUM (CASE WHEN a.status = :absent THEN 1 ELSE 0 END ) as absent,"
			+ "  COALESCE(SUM (a.penaltyScore), 0) as penaltyScore "
			+ "FROM AttendEntity a JOIN MemberEntity m ON a.memberId=m.id WHERE a.createdDate >= :startDate AND a.createdDate <= :endDate "
			+ "GROUP BY m.id, m.name")
	Page<Object[]> getStatistics(
			@Param("startDate") Timestamp startDate,
			@Param("endDate") Timestamp endDate,
			@Param("late") AttendStatus late,
			@Param("absent") AttendStatus absent,
			Pageable pageable);
}
