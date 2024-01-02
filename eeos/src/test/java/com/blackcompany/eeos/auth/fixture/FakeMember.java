package com.blackcompany.eeos.auth.fixture;

import com.blackcompany.eeos.auth.application.domain.OauthServerType;
import com.blackcompany.eeos.member.persistence.MemberEntity;

public class FakeMember {
	public static MemberEntity memberEntity() {
		return MemberEntity.builder()
				.id(1L)
				.name("name")
				.oauthServerType(OauthServerType.SLACK)
				.build();
	}
}
