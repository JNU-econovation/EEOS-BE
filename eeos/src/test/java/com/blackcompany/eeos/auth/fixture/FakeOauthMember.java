package com.blackcompany.eeos.auth.fixture;

import com.blackcompany.eeos.auth.application.domain.OauthMemberModel;
import com.blackcompany.eeos.auth.application.domain.OauthServerType;
import com.blackcompany.eeos.auth.persistence.OauthInfoEntity;

public class FakeOauthMember {
	public static OauthMemberModel oauthMemberModel() {
		return OauthMemberModel.builder()
				.oauthId("oauthId")
				.name("name")
				.oauthServerType(OauthServerType.SLACK)
				.build();
	}

	public static OauthInfoEntity oauthInfoEntity() {
		return OauthInfoEntity.builder().oauthId("oauthId").memberId(1L).build();
	}
}
