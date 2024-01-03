package com.blackcompany.eeos.auth.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthInfoRepository extends JpaRepository<AuthInfoEntity, Long> {
	Optional<AuthInfoEntity> findByMemberIdAndToken(Long memberId, String token);

	Optional<AuthInfoEntity> findByToken(String token);
}
