package com.blackcompany.eeos.auth.infra.oauth.slack.converter;

import com.blackcompany.eeos.auth.application.domain.OauthMemberModel;
import com.blackcompany.eeos.auth.application.domain.OauthServerType;
import org.springframework.stereotype.Component;

@Component
public class OauthModelConverter {
	public OauthMemberModel from(
			final String oauthId, final String name, final OauthServerType type) {
		return OauthMemberModel.builder()
				.oauthId(oauthId)
				.name(name)
				.oauthServerType(OauthServerType.find(type))
				.build();
	}
}
