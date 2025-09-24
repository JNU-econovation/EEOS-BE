package com.blackcompany.eeos.auth.persistence.oauth;

import com.blackcompany.eeos.auth.application.domain.OauthMemberModel;
import com.blackcompany.eeos.auth.application.domain.converter.OauthMemberEntityConverter;
import com.blackcompany.eeos.auth.application.repository.OAuthMemberRepository;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class OAuthMemberRepositoryImpl implements OAuthMemberRepository {
	private final JpaOAuthMemberRepository jpaRepository;
	private final OauthMemberEntityConverter converter;

	@Override
	public Optional<OauthMemberModel> findByOauthId(String oauthId) {
		return jpaRepository.findByOauthId(oauthId).map(converter::from);
	}

	@Override
	public Optional<OAuthMemberEntity> findByAccount(String loginId) {
		return jpaRepository.findByAccount(loginId);
	}

	@Override
	public OauthMemberModel save(OauthMemberModel model) {
		OAuthMemberEntity entity = converter.toEntity(model);
		return converter.from(jpaRepository.save(entity));
	}

	@Override
	public void delete(OauthMemberModel model) {
		OAuthMemberEntity entity = converter.toEntity(model);
		jpaRepository.delete(entity);
	}

	@Override
	public void deleteById(Long memberId) {
		jpaRepository.deleteById(memberId);
	}

	@Override
	public Boolean existsById(Long memberId) {
		return jpaRepository.existsById(memberId);
	}
}
