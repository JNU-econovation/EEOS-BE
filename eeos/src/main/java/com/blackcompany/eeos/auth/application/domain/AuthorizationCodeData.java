package com.blackcompany.eeos.auth.application.domain;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorizationCodeData implements Serializable {
	private Long memberId;
	private String clientId;
	private String codeChallenge;
	private String codeChallengeMethod;
	private String redirectUri;
}
