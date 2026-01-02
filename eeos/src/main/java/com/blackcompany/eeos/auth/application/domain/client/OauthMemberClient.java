package com.blackcompany.eeos.auth.application.domain.client;

import com.blackcompany.eeos.auth.application.domain.OauthMemberModel;
import com.blackcompany.eeos.auth.application.domain.OauthServerType;
import com.blackcompany.eeos.common.presentation.support.AuthorizationScheme;

public interface OauthMemberClient {
	default String formatBearerToken(String token) {
		return AuthorizationScheme.formatBearerToken(token);
	}

	OauthServerType support();

	OauthMemberModel fetch(String code, String uri);
}
