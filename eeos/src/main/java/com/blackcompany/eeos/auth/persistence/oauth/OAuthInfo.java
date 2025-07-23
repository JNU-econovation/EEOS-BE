package com.blackcompany.eeos.auth.persistence.oauth;

import com.blackcompany.eeos.auth.application.domain.OauthServerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OAuthInfo {
	private String oauthId;
	private OauthServerType oauthServerType;
	private boolean requiresAdditionalInfo;
}
