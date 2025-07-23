package com.blackcompany.eeos.auth.application.repository;

import com.blackcompany.eeos.auth.application.domain.OauthMemberModel;
import com.blackcompany.eeos.auth.persistence.oauth.OAuthInfo;
import java.util.UUID;

public interface OauthVerificationStorage {
	UUID store(OauthMemberModel model);

	OAuthInfo get(UUID uuid);
}
