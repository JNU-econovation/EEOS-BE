package com.blackcompany.eeos.auth.persistence;

import com.blackcompany.eeos.auth.application.domain.OauthServerType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OAuthInfo {
	private String oauthId;
	private OauthServerType oauthServerType;
	private boolean requiresAdditionalInfo;
}
