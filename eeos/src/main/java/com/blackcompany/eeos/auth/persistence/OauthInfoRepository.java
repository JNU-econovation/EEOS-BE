package com.blackcompany.eeos.auth.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OauthInfoRepository extends JpaRepository<OauthInfoEntity, Long> {
	Optional<OauthInfoEntity> findByOauthId(String oauthId);
}
