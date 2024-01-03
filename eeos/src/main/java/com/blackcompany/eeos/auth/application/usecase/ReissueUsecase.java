package com.blackcompany.eeos.auth.application.usecase;

import com.blackcompany.eeos.auth.application.domain.TokenModel;

public interface ReissueUsecase {
	TokenModel execute(final String token);
}
