package com.blackcompany.eeos.attend.persistence;

import com.blackcompany.eeos.attend.application.model.AttendStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendRepository extends JpaRepository<AttendEntity, Long> {
	List<AttendEntity> findAllByProgramIdAndStatus(Long programId, AttendStatus status);

	Optional<AttendEntity> findByProgramIdAndMemberId(Long programId, Long memberId);

	void deleteAllByProgramId(Long programId);
}
