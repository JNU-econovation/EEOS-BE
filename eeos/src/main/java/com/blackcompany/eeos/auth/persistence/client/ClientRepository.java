package com.blackcompany.eeos.auth.persistence.client;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ClientRepository extends JpaRepository<ClientEntity, Long> {

	@Query("SELECT c FROM ClientEntity c LEFT JOIN FETCH c.redirectUris WHERE c.clientId = :clientId")
	Optional<ClientEntity> findByClientIdWithRedirectUris(String clientId);
}
