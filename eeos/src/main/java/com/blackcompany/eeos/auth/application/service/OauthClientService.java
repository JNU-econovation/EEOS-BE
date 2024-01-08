package com.blackcompany.eeos.auth.application.service;

import com.blackcompany.eeos.auth.application.domain.OauthMemberModel;
import com.blackcompany.eeos.auth.application.domain.client.OauthMemberClientComposite;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OauthClientService {
	private final OauthMemberClientComposite oauthMemberClientComposite;

	@Transactional(propagation = Propagation.REQUIRED)
	public OauthMemberModel getOauthMember(String oauthServerType, String authCode, String uri) {
		OauthMemberModel model = oauthMemberClientComposite.fetch(oauthServerType, authCode, uri);
		model.validateNameFormat();

		return model;
	}
}
