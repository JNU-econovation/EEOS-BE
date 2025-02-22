package com.blackcompany.eeos.auth.application.service;

import com.blackcompany.eeos.auth.application.domain.OauthMemberModel;
import com.blackcompany.eeos.auth.application.domain.TokenModel;
import com.blackcompany.eeos.auth.application.dto.request.OAuthLoginRequestCommand;
import com.blackcompany.eeos.auth.application.support.AuthenticationTokenGenerator;
import com.blackcompany.eeos.auth.application.usecase.LoginUsecase;
import com.blackcompany.eeos.auth.persistence.OAuthMemberEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthFacadeService implements LoginUsecase {
	private final OauthClientService oauthClientService;
	private final AuthService authService;
	private final AuthenticationTokenGenerator authenticationTokenGenerator;

	@Override
	public TokenModel login(OAuthLoginRequestCommand command) {
		OauthMemberModel model =
				oauthClientService.getOauthMember(command.oauthServerType(), command.code(), command.uri());
		Long memberId = authService.authenticate(model);

		return authenticationTokenGenerator.execute(memberId);
	}

	@Override
	public TokenModel login(String loginId, String password) {
		OAuthMemberEntity entity = authService.authenticate(loginId, password);
		return authenticationTokenGenerator.execute(entity.getMemberId());
	}
}
