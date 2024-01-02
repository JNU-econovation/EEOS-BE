package com.blackcompany.eeos.auth.fixture;

import com.blackcompany.eeos.auth.application.domain.OauthMemberModel;
import com.blackcompany.eeos.auth.application.domain.OauthServerType;
import com.blackcompany.eeos.auth.application.domain.client.OauthMemberClient;

public class FakeOauthMemberClient implements OauthMemberClient {
	@Override
	public OauthServerType support() {
		return OauthServerType.SLACK;
	}

	@Override
	public OauthMemberModel fetch(String code) {
		return OauthMemberModel.builder()
				.oauthId("oauth_id")
				.oauthServerType(OauthServerType.SLACK)
				.build();
	}
}
