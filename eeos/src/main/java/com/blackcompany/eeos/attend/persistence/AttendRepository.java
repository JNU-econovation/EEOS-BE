package com.blackcompany.eeos.attend.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendRepository extends JpaRepository<AttendEntity, Long> {
	List<AttendEntity> findAllByProgramId(Long programId);
}
