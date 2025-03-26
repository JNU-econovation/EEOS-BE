package com.blackcompany.eeos.target.persistence;

import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramRankCounterRepository
		extends JpaRepository<ProgramRankCounterEntity, Long> {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT c FROM ProgramRankCounterEntity c WHERE c.programId = :programId")
	Optional<ProgramRankCounterEntity> findByProgramIdForUpdate(@Param("programId") Long programId);
}
