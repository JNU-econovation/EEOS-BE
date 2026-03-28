package com.blackcompany.eeos.auth.persistence.client;

import com.blackcompany.eeos.auth.application.domain.ClientType;
import com.blackcompany.eeos.common.persistence.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = ClientEntity.ENTITY_PREFIX)
public class ClientEntity extends BaseEntity {
	public static final String ENTITY_PREFIX = "oauth_client";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = ENTITY_PREFIX + "_id", nullable = false)
	private Long id;

	@Column(name = ENTITY_PREFIX + "_client_id", nullable = false, unique = true, length = 36)
	private String clientId;

	@Column(name = ENTITY_PREFIX + "_client_secret")
	private String clientSecret;

	@Column(name = ENTITY_PREFIX + "_client_name", nullable = false, length = 100)
	private String clientName;

	@Enumerated(EnumType.STRING)
	@Column(name = ENTITY_PREFIX + "_client_type", nullable = false, length = 10)
	private ClientType clientType;

	@OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<ClientRedirectUriEntity> redirectUris = new ArrayList<>();

	public void addRedirectUri(String uri) {
		redirectUris.add(ClientRedirectUriEntity.builder().client(this).redirectUri(uri).build());
	}

	public boolean hasRedirectUri(String uri) {
		return redirectUris.stream().anyMatch(r -> r.getRedirectUri().equals(uri));
	}
}
