package com.blackcompany.eeos.member.application.model.converter;

import com.blackcompany.eeos.auth.application.domain.OauthServerType;
import com.blackcompany.eeos.common.support.converter.AbstractEntityConverter;
import com.blackcompany.eeos.member.application.model.ActiveStatus;
import com.blackcompany.eeos.member.application.model.MemberModel;
import com.blackcompany.eeos.member.persistence.MemberEntity;
import org.springframework.stereotype.Component;

@Component
public class MemberEntityConverter implements AbstractEntityConverter<MemberEntity, MemberModel> {

	@Override
	public MemberModel from(MemberEntity source) {
		return MemberModel.builder()
				.id(source.getId())
				.name(source.getName())
				.activeStatus(source.getActiveStatus())
				.isAdmin(source.isAdmin())
				.oauthServerType(source.getOauthServerType())
				.build();
	}

	@Override
	public MemberEntity toEntity(MemberModel source) {
		return MemberEntity.builder()
				.id(source.getId())
				.name(source.getName())
				.isAdmin(source.isAdmin())
				.activeStatus(ActiveStatus.find(source.getActiveStatus()))
				.oauthServerType(source.getOauthServerType())
				.build();
	}

	public MemberEntity toEntity(String name, OauthServerType oauthServerType) {
		return MemberEntity.builder().name(name).oauthServerType(oauthServerType).build();
	}
}
