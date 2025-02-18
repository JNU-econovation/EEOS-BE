package com.blackcompany.eeos.auth.fixture;

import com.blackcompany.eeos.auth.application.domain.OauthMemberModel;
import com.blackcompany.eeos.auth.application.domain.OauthServerType;
import com.blackcompany.eeos.auth.persistence.OAuthMemberEntity;

public class FakeOauthMember {
	public static OauthMemberModel oauthMemberModel(OauthServerType type, Long memberId) {
		return OauthMemberModel.builder()
				.oauthId("oauthId")
				.name("name")
				.oauthServerType(type)
				.memberId(memberId)
				.build();
	}

	public static OauthMemberModel oauthMemberModel(OauthServerType type) {
		return OauthMemberModel.builder().oauthId("oauthId").name("name").oauthServerType(type).build();
	}

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
