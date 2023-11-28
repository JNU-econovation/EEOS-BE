package com.blackcompany.eeos.program.persistence;

import java.sql.Timestamp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProgramRepository extends JpaRepository<ProgramEntity, Long> {

	@Query(
			"SELECT p FROM ProgramEntity p WHERE p.programDate < :now ORDER BY p.programDate ASC, p.title ASC")
	Page<ProgramEntity> findAllByEnd(@Param("now") Timestamp now, Pageable pageable);

	@Query(
			"SELECT p FROM ProgramEntity p WHERE p.programDate >= :now ORDER BY p.programDate DESC, p.title ASC")
	Page<ProgramEntity> findAllByIng(@Param("now") Timestamp now, Pageable pageable);
}
