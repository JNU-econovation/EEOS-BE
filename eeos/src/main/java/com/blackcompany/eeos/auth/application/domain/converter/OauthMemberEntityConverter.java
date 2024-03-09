package com.blackcompany.eeos.auth.application.domain.converter;

import com.blackcompany.eeos.auth.application.domain.OauthMemberModel;
import com.blackcompany.eeos.auth.persistence.OAuthMemberEntity;
import com.blackcompany.eeos.common.support.converter.AbstractEntityConverter;
import org.springframework.stereotype.Component;

@Component
public class OauthMemberEntityConverter
		implements AbstractEntityConverter<OAuthMemberEntity, OauthMemberModel> {
	@Override
	public OauthMemberModel from(final OAuthMemberEntity entity) {
		return OauthMemberModel.builder().oauthId(entity.getOauthId()).build();
	}

	@Override
	public OAuthMemberEntity toEntity(final OauthMemberModel model) {
		return OAuthMemberEntity.builder().oauthId(model.getOauthId()).build();
	}

	public OAuthMemberEntity toEntity(final String oauthId, Long memberId) {
		return OAuthMemberEntity.builder().oauthId(oauthId).memberId(memberId).build();
	}
}
