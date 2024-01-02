package com.blackcompany.eeos.auth.application.domain.converter;

import com.blackcompany.eeos.auth.application.domain.OauthMemberModel;
import com.blackcompany.eeos.auth.persistence.OauthInfoEntity;
import com.blackcompany.eeos.common.support.converter.AbstractEntityConverter;
import org.springframework.stereotype.Component;

@Component
public class OauthInfoEntityConverter
		implements AbstractEntityConverter<OauthInfoEntity, OauthMemberModel> {
	@Override
	public OauthMemberModel from(final OauthInfoEntity oauthEntity) {
		return OauthMemberModel.builder().oauthId(oauthEntity.getOauthId()).build();
	}

	@Override
	public OauthInfoEntity toEntity(final OauthMemberModel oauthMemberModel) {
		return OauthInfoEntity.builder().oauthId(oauthMemberModel.getOauthId()).build();
	}

	public OauthInfoEntity toEntity(final String oauthId, Long memberId) {
		return OauthInfoEntity.builder().oauthId(oauthId).memberId(memberId).build();
	}
}
