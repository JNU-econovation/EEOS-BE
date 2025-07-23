package com.blackcompany.eeos.auth.application.service;

import com.blackcompany.eeos.auth.application.domain.OauthMemberModel;
import com.blackcompany.eeos.auth.application.exception.NotFoundAccountException;
import com.blackcompany.eeos.auth.application.model.AccountModel;
import com.blackcompany.eeos.auth.application.model.AuthorityModel;
import com.blackcompany.eeos.auth.application.model.Role;
import com.blackcompany.eeos.auth.application.repository.AccountRepository;
import com.blackcompany.eeos.auth.application.repository.AuthorityRepository;
import com.blackcompany.eeos.auth.application.repository.OAuthMemberRepository;
import com.blackcompany.eeos.auth.application.support.EncryptHelper;
import com.blackcompany.eeos.member.application.model.MemberModel;
import com.blackcompany.eeos.member.application.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AuthService {
	private final MemberRepository memberRepository;
	private final OAuthMemberRepository oAuthMemberRepository;
	private final EncryptHelper encryptHelper;
	private final AccountRepository accountRepository;
	private final AuthorityRepository authorityRepository;

	@Transactional
	public Long authenticate(final OauthMemberModel model) {
		return oAuthMemberRepository
				.findByOauthId(model.getOauthId())
				.orElseGet(() -> signUpMember(model))
				.getMemberId();
	}

	@Transactional
	public MemberModel authenticate(final String loginId, final String password) {
		AccountModel accountModel = accountRepository.findByLoginId(loginId);

		String encryptedPassword = accountModel.getPassword();

		checkPassword(password, encryptedPassword);

		return memberRepository.findById(accountModel.getMemberId());
	}

	private OauthMemberModel signUpMember(final OauthMemberModel model) {
		// Slack 신규 유저는 막기
		//		if (model.getOauthServerType() == OauthServerType.SLACK) {
		//			throw new OAuthSignupRestrictedException(OauthServerType.SLACK.getOauthServer());
		//		}

		MemberModel member =
				MemberModel.builder()
						.name(model.getName())
						.oauthServerType(model.getOauthServerType())
						.build();
		MemberModel savedMember = memberRepository.save(member);

		authorityRepository.save(AuthorityModel.create(savedMember.getMemberId(), Role.USER));

		OauthMemberModel updatedModel = model.toBuilder().memberId(savedMember.getId()).build();
		return oAuthMemberRepository.save(updatedModel);
	}

	private void checkPassword(String password, String encryptedPassword) {
		if (!encryptHelper.isMatch(password, encryptedPassword)) throw new NotFoundAccountException();
	}
}
