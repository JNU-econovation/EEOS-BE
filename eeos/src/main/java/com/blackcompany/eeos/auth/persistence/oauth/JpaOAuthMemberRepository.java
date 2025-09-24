package com.blackcompany.eeos.auth.persistence.oauth;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaOAuthMemberRepository extends JpaRepository<OAuthMemberEntity, Long> {
	@Query("SELECT o FROM OAuthMemberEntity  o WHERE o.oauthId=:oauthId")
	Optional<OAuthMemberEntity> findByOauthId(@Param("oauthId") String oauthId);

	@Query("SELECT o FROM OAuthMemberEntity  o WHERE o.memberId=:memberId")
	Optional<OAuthMemberEntity> findByMemberId(@Param("memberId") Long memberId);

	@Query(
			"SELECT o FROM OAuthMemberEntity  o WHERE o.memberId = (SELECT a.memberId FROM AccountEntity a WHERE a.loginId=:loginId)")
	Optional<OAuthMemberEntity> findByAccount(@Param("loginId") String loginId); // TODO : 쿼리 확인
}
