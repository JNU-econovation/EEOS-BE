package com.blackcompany.eeos.auth.application.domain.client;

import com.blackcompany.eeos.auth.application.domain.OauthMemberModel;
import com.blackcompany.eeos.auth.application.domain.OauthServerType;

public interface OauthMemberClient {
	OauthServerType support();

	OauthMemberModel fetch(String code, String uri);
}
