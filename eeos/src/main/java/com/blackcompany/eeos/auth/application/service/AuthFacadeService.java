package com.blackcompany.eeos.auth.application.service;

import com.blackcompany.eeos.auth.application.domain.OauthMemberModel;
import com.blackcompany.eeos.auth.application.domain.TokenModel;
import com.blackcompany.eeos.auth.application.domain.client.OauthMemberClientComposite;
import com.blackcompany.eeos.auth.application.usecase.LoginUsecase;
import com.blackcompany.eeos.auth.persistence.OauthInfoEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthFacadeService implements LoginUsecase {
	private final OauthMemberClientComposite oauthMemberClientComposite;
	private final AuthService authService;
	private final CreateTokenService createTokenService;

	@Override
	public TokenModel login(String oauthServerType, String authCode) {
		OauthMemberModel model = oauthMemberClientComposite.fetch(oauthServerType, authCode);
		OauthInfoEntity entity = authService.login(model);
		return createTokenService.execute(entity.getMemberId());
	}
}
