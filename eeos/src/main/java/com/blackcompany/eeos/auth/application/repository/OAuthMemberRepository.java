package com.blackcompany.eeos.auth.application.repository;

import com.blackcompany.eeos.auth.application.domain.OauthMemberModel;
import com.blackcompany.eeos.auth.persistence.oauth.OAuthMemberEntity;
import java.util.Optional;

public interface OAuthMemberRepository {
	Optional<OauthMemberModel> findByOauthId(String oauthId);

	Optional<OauthMemberModel> findByMemberId(Long memberId);

	Optional<OAuthMemberEntity> findByAccount(String loginId);

	OauthMemberModel save(OauthMemberModel model);

	void delete(OauthMemberModel model);

	void deleteById(Long memberId);

	Boolean existsById(Long memberId);
}
