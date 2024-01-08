package com.blackcompany.eeos.auth.application.service;

import com.blackcompany.eeos.auth.application.domain.OauthMemberModel;
import com.blackcompany.eeos.auth.application.domain.TokenModel;
import com.blackcompany.eeos.auth.application.usecase.LoginUsecase;
import com.blackcompany.eeos.auth.persistence.OauthInfoEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthFacadeService implements LoginUsecase {
	private final OauthClientService oauthClientService;
	private final AuthService authService;
	private final CreateTokenService createTokenService;

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public TokenModel login(String oauthServerType, String authCode, String uri) {
		OauthMemberModel model = oauthClientService.getOauthMember(oauthServerType, authCode, uri);
		OauthInfoEntity entity = authService.login(model);
		return createTokenService.execute(entity.getMemberId());
	}
}
