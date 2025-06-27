package com.blackcompany.eeos.auth.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountJpaRepository extends JpaRepository<AccountEntity, Long> {

	@Query("SELECT a.passWd FROM AccountEntity a WHERE a.loginId=:loginId")
	Optional<String> findPasswdByLoginId(@Param("loginId") String loginId);

	@Query("SELECT a FROM AccountEntity a WHERE a.loginId=:loginId")
	Optional<AccountEntity> findByLoginId(@Param("loginId") String loginId);
}
