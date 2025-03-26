package com.blackcompany.eeos.auth.application.service;

import com.blackcompany.eeos.auth.application.domain.OauthMemberModel;
import com.blackcompany.eeos.auth.application.domain.client.OauthMemberClientComposite;
import com.blackcompany.eeos.auth.application.exception.RequiredSignupInfoException;
import com.blackcompany.eeos.auth.application.repository.OauthVerificationStorage;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OauthClientService {
	private final OauthMemberClientComposite oauthMemberClientComposite;
	private final OauthVerificationStorage oauthVerificationStorage;

	public OauthMemberModel getOauthMember(String oauthServerType, String authCode, String uri) {
		OauthMemberModel model = oauthMemberClientComposite.fetch(oauthServerType, authCode, uri);

		if (model.isRequiresAdditionalInfo()) {
			UUID tempId = oauthVerificationStorage.store(model);
			throw new RequiredSignupInfoException(tempId);
		}
		model.validateNameFormat();

		return model;
	}
}
