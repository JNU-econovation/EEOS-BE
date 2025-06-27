package com.blackcompany.eeos.auth.persistence;

import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AuthorityJpaRepository extends JpaRepository<Authority, Long> {

	@Query("SELECT DISTINCT a FROM Authority a WHERE a.memberId = :memberId")
	Set<Authority> findByMemberId(@Param("memberId") Long memberId);
}
