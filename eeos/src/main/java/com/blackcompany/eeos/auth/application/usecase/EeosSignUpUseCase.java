package com.blackcompany.eeos.auth.application.usecase;

import com.blackcompany.eeos.auth.application.domain.TokenModel;
import com.blackcompany.eeos.auth.application.dto.request.EeosSignUpCommand;

public interface EeosSignUpUseCase {
	TokenModel signUp(EeosSignUpCommand command);
}
