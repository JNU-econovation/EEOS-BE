package com.blackcompany.eeos.auth.application.service;

import com.blackcompany.eeos.auth.application.domain.OauthMemberModel;
import com.blackcompany.eeos.auth.application.domain.client.OauthMemberClientComposite;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OauthClientService {
	private final OauthMemberClientComposite oauthMemberClientComposite;

	public OauthMemberModel getOauthMember(String oauthServerType, String authCode, String uri) {
		OauthMemberModel model = oauthMemberClientComposite.fetch(oauthServerType, authCode, uri);
		model.validateNameFormat();

		return model;
	}
}
