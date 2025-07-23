package com.blackcompany.eeos.auth.application.service;

import com.blackcompany.eeos.auth.application.domain.OauthMemberModel;
import com.blackcompany.eeos.auth.application.domain.TokenModel;
import com.blackcompany.eeos.auth.application.dto.request.AdditionalInfoApplicationCommand;
import com.blackcompany.eeos.auth.application.model.AuthorityModel;
import com.blackcompany.eeos.auth.application.model.Role;
import com.blackcompany.eeos.auth.application.repository.AuthorityRepository;
import com.blackcompany.eeos.auth.application.repository.OAuthMemberRepository;
import com.blackcompany.eeos.auth.application.repository.OauthVerificationStorage;
import com.blackcompany.eeos.auth.application.support.AuthenticationTokenGenerator;
import com.blackcompany.eeos.auth.application.usecase.OAuthSignUpUseCase;
import com.blackcompany.eeos.auth.persistence.oauth.OAuthInfo;
import com.blackcompany.eeos.member.application.model.ActiveStatus;
import com.blackcompany.eeos.member.application.model.MemberModel;
import com.blackcompany.eeos.member.application.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SignUpService implements OAuthSignUpUseCase {
	private final OauthVerificationStorage verificationStorage;
	private final MemberRepository memberRepository;
	private final OAuthMemberRepository oAuthMemberRepository;
	private final AuthenticationTokenGenerator tokenGenerator;
	private final AuthorityRepository authorityRepository;

	@Override
	@Transactional
	public TokenModel signUp(AdditionalInfoApplicationCommand command) {
		OAuthInfo oAuthInfo = verificationStorage.get(command.getVerificationId());

		MemberModel memberModel =
				MemberModel.builder()
						.name(command.getName(), command.getGeneration())
						.activeStatus(ActiveStatus.find(command.getActiveStatus()))
						.oauthServerType(oAuthInfo.getOauthServerType())
						.build();
		MemberModel savedMember = memberRepository.save(memberModel);

		saveOAuth(oAuthInfo.getOauthId(), savedMember.getMemberId());
		saveAuthority(savedMember.getMemberId(), "ROLE_" + Role.USER.getRole());

		// TODO: 일반 USER 권한인지 아닌지 계산해주는 도구 추가

		return tokenGenerator.execute(savedMember.getId());
	}

	private void saveOAuth(String oAuthId, Long memberId) {
		OauthMemberModel oauthMemberModel =
				OauthMemberModel.builder().oauthId(oAuthId).memberId(memberId).build();

		oAuthMemberRepository.save(oauthMemberModel);
	}

	private void saveAuthority(Long memberId, String role) {
		AuthorityModel authorityModel = AuthorityModel.builder().memberId(memberId).name(role).build();

		authorityRepository.save(authorityModel);
	}
}
