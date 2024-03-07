package com.blackcompany.eeos.auth.fixture;

import com.blackcompany.eeos.auth.application.domain.OauthMemberModel;
import com.blackcompany.eeos.auth.application.domain.OauthServerType;
import com.blackcompany.eeos.auth.persistence.OAuthMemberEntity;

public class FakeOauthMember {
	public static OauthMemberModel oauthMemberModel() {
		return OauthMemberModel.builder()
				.oauthId("oauthId")
				.name("name")
				.oauthServerType(OauthServerType.SLACK)
				.build();
	}

	public static OAuthMemberEntity oauthInfoEntity() {
		return OAuthMemberEntity.builder().oauthId("oauthId").memberId(1L).build();
	}
}
