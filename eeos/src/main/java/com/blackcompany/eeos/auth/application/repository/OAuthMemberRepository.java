package com.blackcompany.eeos.auth.application.repository;

import com.blackcompany.eeos.auth.application.domain.OauthMemberModel;
import com.blackcompany.eeos.auth.persistence.OAuthMemberEntity;
import java.util.Optional;

public interface OAuthMemberRepository {
	Optional<OauthMemberModel> findByOauthId(String oauthId);

	Optional<OAuthMemberEntity> findByAccount(String loginId);

	OauthMemberModel save(OauthMemberModel model);

	void delete(OauthMemberModel model);

	void deleteById(Long memberId);

	Boolean existsById(Long memberId);
}
