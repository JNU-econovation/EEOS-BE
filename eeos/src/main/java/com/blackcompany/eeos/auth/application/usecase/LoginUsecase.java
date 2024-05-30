package com.blackcompany.eeos.auth.application.usecase;

import com.blackcompany.eeos.auth.application.domain.TokenModel;

public interface LoginUsecase {
	TokenModel login(String oauthServerType, String authCode, String uri);

	TokenModel login(String loginId, String password);
}
