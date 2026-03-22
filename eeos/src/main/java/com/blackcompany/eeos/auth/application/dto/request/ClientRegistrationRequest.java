package com.blackcompany.eeos.auth.application.dto.request;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClientRegistrationRequest {
	private String clientName;
	private String clientType;
	private Set<String> redirectUris;
}
