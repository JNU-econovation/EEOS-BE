package com.blackcompany.eeos.target.persistence;

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

}
