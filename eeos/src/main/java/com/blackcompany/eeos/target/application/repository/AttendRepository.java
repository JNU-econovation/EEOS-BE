package com.blackcompany.eeos.target.application.repository;

import com.blackcompany.eeos.target.application.model.AttendStatus;
import com.blackcompany.eeos.target.persistence.AttendEntity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AttendRepository {

	List<AttendEntity> findAllByProgramIdAndStatus(Long programId, AttendStatus status);

	Optional<AttendEntity> findByProgramIdAndMemberId(Long programId, Long memberId);

	void deleteAllByProgramId(Long programId);

	List<AttendEntity> findAllByProgramMember(Long programId, List<Long> memberIds);

	List<AttendEntity> findAllByProgramId(Long programId);

	void deleteAllByMemberId(Long memberId);

	void updateAttendStatusByProgramId(
			Long programId, AttendStatus beforeStatus, AttendStatus afterStatus);

	long countAttendStatusByProgramIdAndStatus(Long programId, AttendStatus status);

	List<AttendEntity> findByProgramIdsAndMemberId(List<Long> programIds, Long memberId);

	List<Long> findByPenaltyPointSum(LocalDateTime startDate, LocalDateTime endDate, Long limit);
}
