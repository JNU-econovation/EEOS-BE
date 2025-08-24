package com.blackcompany.eeos.auth.persistence.authority;

import com.blackcompany.eeos.auth.application.model.Role;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AuthorityJpaRepository extends JpaRepository<AuthorityEntity, Long> {

	@Query("SELECT DISTINCT a FROM AuthorityEntity a WHERE a.memberId = :memberId")
	Set<AuthorityEntity> findByMemberId(@Param("memberId") Long memberId);

	@Query("SELECT a FROM AuthorityEntity a WHERE a.memberId=:memberId AND a.role=:role")
	Optional<AuthorityEntity> findByIdAndRole(
			@Param("memberId") Long memberId, @Param("role") Role role);
}
