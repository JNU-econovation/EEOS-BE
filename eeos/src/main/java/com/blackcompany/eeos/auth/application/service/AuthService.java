package com.blackcompany.eeos.auth.application.service;

import com.blackcompany.eeos.auth.application.domain.OauthMemberModel;
import com.blackcompany.eeos.auth.application.domain.converter.OauthInfoEntityConverter;
import com.blackcompany.eeos.auth.persistence.OauthInfoEntity;
import com.blackcompany.eeos.auth.persistence.OauthInfoRepository;
import com.blackcompany.eeos.member.application.model.converter.MemberEntityConverter;
import com.blackcompany.eeos.member.persistence.MemberEntity;
import com.blackcompany.eeos.member.persistence.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

	private final OauthInfoRepository oauthInfoRepository;
	private final MemberRepository memberRepository;
	private final MemberEntityConverter memberEntityConverter;
	private final OauthInfoEntityConverter oauthInfoEntityConverter;

	@Transactional
	public OauthInfoEntity login(final OauthMemberModel model) {
		return oauthInfoRepository
				.findByOauthId(model.getOauthId())
				.orElseGet(() -> signUpMember(model));
	}

	private OauthInfoEntity signUpMember(final OauthMemberModel model) {
		MemberEntity entity =
				memberEntityConverter.toEntity(model.getName(), model.getOauthServerType());

		MemberEntity savedMember = memberRepository.save(entity);
		return createOauthInfoEntity(model.getOauthId(), savedMember.getId());
	}

	private OauthInfoEntity createOauthInfoEntity(final String oauthId, final Long memberId) {
		OauthInfoEntity entity = oauthInfoEntityConverter.toEntity(oauthId, memberId);
		return oauthInfoRepository.save(entity);
	}
}
