package com.blackcompany.eeos.member.fixture;

import com.blackcompany.eeos.auth.application.domain.OauthServerType;
import com.blackcompany.eeos.member.application.model.ActiveStatus;
import com.blackcompany.eeos.member.application.model.MemberModel;
import com.blackcompany.eeos.member.persistence.MemberEntity;

public class FakeMember {
	public static MemberEntity AmMemberEntity() {
		return MemberEntity.builder()
				.id(1L)
				.name("mando")
				.oauthServerType(OauthServerType.SLACK)
				.activeStatus(ActiveStatus.AM)
				.build();
	}

	public static MemberModel AmMemberModel() {
		return MemberModel.builder().id(1L).name("mando").activeStatus(ActiveStatus.AM).build();
	}

	public static MemberEntity RmMemberEntity() {
		return MemberEntity.builder()
				.id(1L)
				.name("mando")
				.oauthServerType(OauthServerType.SLACK)
				.activeStatus(ActiveStatus.RM)
				.build();
	}
}
