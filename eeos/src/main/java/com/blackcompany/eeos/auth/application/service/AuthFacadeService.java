package com.blackcompany.eeos.auth.application.service;

import com.blackcompany.eeos.auth.application.domain.OauthMemberModel;
import com.blackcompany.eeos.auth.application.domain.TokenModel;
import com.blackcompany.eeos.auth.application.dto.request.OAuthLoginRequestCommand;
import com.blackcompany.eeos.auth.application.model.AuthorityModel;
import com.blackcompany.eeos.auth.application.model.Role;
import com.blackcompany.eeos.auth.application.repository.AuthorityRepository;
import com.blackcompany.eeos.auth.application.support.AuthenticationTokenGenerator;
import com.blackcompany.eeos.auth.application.usecase.LoginUsecase;
import com.blackcompany.eeos.member.application.model.MemberModel;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthFacadeService implements LoginUsecase {
	private final OauthClientService oauthClientService;
	private final AuthService authService;
	private final AuthenticationTokenGenerator authenticationTokenGenerator;
	private final AuthorityRepository authorityRepository;

	@Override
	public TokenModel login(OAuthLoginRequestCommand command) {
		OauthMemberModel model =
				oauthClientService.getOauthMember(command.oauthServerType(), command.code(), command.uri());
		Long memberId = authService.authenticate(model);

		Set<String> authorities = getRoles(memberId);

		return authenticationTokenGenerator.execute(memberId, authorities);
	}

	@Override
	public TokenModel login(String loginId, String password) {
		MemberModel model = authService.authenticate(loginId, password);
		Long memberId = model.getMemberId();

		Set<String> authorities = getRoles(memberId);

		return authenticationTokenGenerator.execute(memberId, authorities);
	}

	private Set<String> getRoles(Long memberId){
		return authorityRepository.findByMemberId(memberId).stream()
						.map(AuthorityModel::getRole)
						.map(Role::getRole)
						.collect(Collectors.toSet());
	}
}
