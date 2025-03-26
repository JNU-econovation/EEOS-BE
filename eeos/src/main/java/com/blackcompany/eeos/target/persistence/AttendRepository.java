package com.blackcompany.eeos.target.persistence;

import com.blackcompany.eeos.target.application.model.AttendStatus;
import jakarta.persistence.LockModeType;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AttendRepository extends JpaRepository<AttendEntity, Long> {
	@Query(
			"SELECT a FROM AttendEntity  a WHERE a.programId =:programId AND a.status=:status AND a.isDeleted=false")
	List<AttendEntity> findAllByProgramIdAndStatus(
			@Param("programId") Long programId, @Param("status") AttendStatus status);

	@Query(
			"SELECT a FROM AttendEntity  a WHERE a.programId =:programId AND a.memberId=:memberId AND a.isDeleted=false")
	Optional<AttendEntity> findByProgramIdAndMemberId(
			@Param("programId") Long programId, @Param("memberId") Long memberId);

	void deleteAllByProgramId(Long programId);

	@Query(
			"SELECT a FROM AttendEntity  a WHERE a.programId =:programId AND a.memberId IN :ids AND a.isDeleted=false")
	List<AttendEntity> findAllByProgramMember(
			@Param("programId") Long programId, @Param("ids") List<Long> memberIds);

	@Query("SELECT a FROM AttendEntity  a WHERE a.programId =:programId AND a.isDeleted=false")
	List<AttendEntity> findAllByProgramId(@Param("programId") Long programId);

	void deleteAllByMemberId(Long memberId);

	@Modifying
	@Query(
			"UPDATE AttendEntity a SET a.status=:afterStatus WHERE a.programId=:programId AND a.status=:beforeStatus")
	void updateAttendStatusByProgramId(
			@Param("programId") Long programId,
			@Param("beforeStatus") AttendStatus beforeStatus,
			@Param("afterStatus") AttendStatus afterStatus);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query(
			"SELECT COUNT(a) FROM AttendEntity  a WHERE a.programId = :programId AND a.status = :status")
	long countAttendStatusByProgramIdAndStatus(
			@Param("programId") Long programId, @Param("status") AttendStatus status);

	List<AttendEntity> findTop5ByProgramIdAndStatusOrderByRankAsc(
			Long programId, AttendStatus status);

	@Query(
			"SELECT a FROM AttendEntity a WHERE a.programId IN :programIds AND a.memberId=:memberId AND a.isDeleted=false")
	List<AttendEntity> findByProgramIdsAndMemberId(
			@Param("programIds") List<Long> programIds, @Param("memberId") Long memberId);

	@Query("SELECT a FROM AttendEntity a WHERE a.memberId = :memberId AND a.isDeleted = false AND a.createdDate > :startDate AND a.createdDate < :endDate")
	List<AttendEntity> findAllByMemberIdAndCreatedDateGreaterThan(
			@Param("memberId") Long memberId, @Param("createdDate") Timestamp createdDate, @Param("endDate") Timestamp endDate);
}
