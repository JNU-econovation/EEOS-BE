package com.blackcompany.eeos.auth.application.usecase;

import com.blackcompany.eeos.auth.application.domain.TokenModel;
import com.blackcompany.eeos.auth.application.dto.request.AdditionalInfoApplicationCommand;

public interface OAuthSignUpUseCase {
	TokenModel signUp(AdditionalInfoApplicationCommand command);
}
