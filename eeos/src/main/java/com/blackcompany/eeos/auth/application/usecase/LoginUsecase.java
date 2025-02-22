package com.blackcompany.eeos.auth.application.usecase;

import com.blackcompany.eeos.auth.application.domain.TokenModel;
import com.blackcompany.eeos.auth.application.dto.request.OAuthLoginRequestCommand;

public interface LoginUsecase {
	TokenModel login(OAuthLoginRequestCommand command);

	TokenModel login(String loginId, String password);
}
