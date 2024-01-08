package com.blackcompany.eeos.attend.persistence;

import com.blackcompany.eeos.attend.application.model.AttendStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
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
			"SELECT a FROM AttendEntity  a WHERE a.programId =:programId AND a.id IN :ids AND a.isDeleted=false")
	List<AttendEntity> findAllByProgramMember(
			@Param("programId") Long programId, @Param("ids") List<Long> memberIds);

	@Query("SELECT a FROM AttendEntity  a WHERE a.programId =:programId AND a.isDeleted=false")
	List<AttendEntity> findAllByProgramId(@Param("programId") Long programId);
}
