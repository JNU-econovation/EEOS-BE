package com.blackcompany.eeos.auth.persistence.authority;

import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AuthorityJpaRepository extends JpaRepository<AuthorityEntity, Long> {

	@Query("SELECT DISTINCT a FROM AuthorityEntity a WHERE a.memberId = :memberId")
	Set<AuthorityEntity> findByMemberId(@Param("memberId") Long memberId);
}
