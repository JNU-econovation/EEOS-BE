package com.blackcompany.eeos.member.fixture;

import com.blackcompany.eeos.auth.application.domain.OauthServerType;
import com.blackcompany.eeos.member.application.model.ActiveStatus;
import com.blackcompany.eeos.member.application.model.MemberModel;
import com.blackcompany.eeos.member.persistence.MemberEntity;
import java.util.List;

public class FakeMember {
	public static MemberEntity AmMandoEntity() {
		return MemberEntity.builder()
				.id(1L)
				.name("mando")
				.oauthServerType(OauthServerType.SLACK)
				.activeStatus(ActiveStatus.AM)
				.build();
	}

	public static MemberEntity AmBadaEntity() {
		return MemberEntity.builder()
				.id(2L)
				.name("bada")
				.oauthServerType(OauthServerType.SLACK)
				.activeStatus(ActiveStatus.AM)
				.build();
	}

	public static MemberModel AmMandoMemberModel() {
		return MemberModel.builder().id(1L).name("mando").activeStatus(ActiveStatus.AM).build();
	}

	public static MemberModel AmBadaMemberModel() {
		return MemberModel.builder().id(1L).name("bada").activeStatus(ActiveStatus.AM).build();
	}

	public static MemberEntity RmMemberEntity() {
		return MemberEntity.builder()
				.id(1L)
				.name("mando")
				.oauthServerType(OauthServerType.SLACK)
				.activeStatus(ActiveStatus.RM)
				.build();
	}

	public static List<MemberEntity> am_수민_바다() {
		return List.of(am_수민(), am_바다());
	}

	public static MemberEntity am_수민() {
		return MemberEntity.builder()
				.id(1L)
				.name("수민")
				.oauthServerType(OauthServerType.SLACK)
				.activeStatus(ActiveStatus.AM)
				.build();
	}

	public static MemberEntity am_바다() {
		return MemberEntity.builder()
				.id(2L)
				.name("바다")
				.oauthServerType(OauthServerType.SLACK)
				.activeStatus(ActiveStatus.AM)
				.build();
	}
}
