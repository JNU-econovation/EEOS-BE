package com.blackcompany.eeos.auth.application.service;

import com.blackcompany.eeos.auth.application.domain.OauthMemberModel;
import com.blackcompany.eeos.auth.application.domain.TokenModel;
import com.blackcompany.eeos.auth.application.domain.converter.OauthMemberEntityConverter;
import com.blackcompany.eeos.auth.application.support.AuthenticationTokenGenerator;
import com.blackcompany.eeos.auth.application.usecase.LoginUsecase;
import com.blackcompany.eeos.auth.persistence.OAuthMemberEntity;
import com.blackcompany.eeos.auth.persistence.OAuthMemberRepository;
import com.blackcompany.eeos.member.application.model.converter.MemberEntityConverter;
import com.blackcompany.eeos.member.persistence.MemberEntity;
import com.blackcompany.eeos.member.persistence.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthFacadeService implements LoginUsecase {
	private final OauthClientService oauthClientService;
	private final MemberRepository memberRepository;
	private final OAuthMemberRepository oAuthMemberRepository;
	private final AuthenticationTokenGenerator authenticationTokenGenerator;
	private final MemberEntityConverter memberEntityConverter;
	private final OauthMemberEntityConverter oauthMemberEntityConverter;

	@Transactional
	@Override
	public TokenModel login(String oauthServerType, String authCode, String uri) {
		OauthMemberModel model = oauthClientService.getOauthMember(oauthServerType, authCode, uri);
		OAuthMemberEntity entity = login(model);
		return authenticationTokenGenerator.execute(entity.getMemberId());
	}

	private OAuthMemberEntity login(final OauthMemberModel model) {
		return oAuthMemberRepository
				.findByOauthId(model.getOauthId())
				.orElseGet(() -> signUpMember(model));
	}

	private OAuthMemberEntity signUpMember(final OauthMemberModel model) {
		MemberEntity entity =
				memberEntityConverter.toEntity(model.getName(), model.getOauthServerType());

		MemberEntity savedMember = memberRepository.save(entity);
		return saveOauthInfoEntity(model.getOauthId(), savedMember.getId());
	}

	private OAuthMemberEntity saveOauthInfoEntity(final String oauthId, final Long memberId) {
		OAuthMemberEntity entity = oauthMemberEntityConverter.toEntity(oauthId, memberId);
		return oAuthMemberRepository.save(entity);
	}
}
