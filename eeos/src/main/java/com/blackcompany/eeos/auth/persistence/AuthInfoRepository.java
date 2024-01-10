package com.blackcompany.eeos.auth.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AuthInfoRepository extends JpaRepository<AuthInfoEntity, Long> {
	@Query("SELECT a FROM AuthInfoEntity a WHERE a.memberId=:memberId AND a.token =:token")
	Optional<AuthInfoEntity> findByMemberIdAndToken(
			@Param("memberId") Long memberId, @Param("token") String token);

	@Query("SELECT a FROM AuthInfoEntity a WHERE  a.token =:token")
	Optional<AuthInfoEntity> findByToken(@Param("token") String token);
}
