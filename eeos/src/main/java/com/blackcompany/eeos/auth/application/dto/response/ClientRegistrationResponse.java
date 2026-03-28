package com.blackcompany.eeos.auth.application.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClientRegistrationResponse {
	private String clientId;
	private String clientSecret;
}
