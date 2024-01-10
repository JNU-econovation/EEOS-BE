package com.blackcompany.eeos.auth.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OauthInfoRepository extends JpaRepository<OauthInfoEntity, Long> {
	@Query("SELECT o FROM OauthInfoEntity  o WHERE o.oauthId=:oauthId")
	Optional<OauthInfoEntity> findByOauthId(@Param("oauthId") String oauthId);
}
