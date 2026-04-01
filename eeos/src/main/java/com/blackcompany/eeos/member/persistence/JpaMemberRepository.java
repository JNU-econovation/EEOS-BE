package com.blackcompany.eeos.member.persistence;

import com.blackcompany.eeos.auth.application.domain.OauthServerType;
import com.blackcompany.eeos.member.application.model.ActiveStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaMemberRepository extends JpaRepository<MemberEntity, Long> {

	@Query(
			"SELECT m FROM MemberEntity m inner join AttendEntity a on m.id = a.memberId where a.programId = :programId AND m.isDeleted=false ORDER BY  m.name")
	List<MemberEntity> findMembersByProgramId(@Param("programId") Long programId);

	@Query(
			"SELECT m FROM MemberEntity m where m.activeStatus= :activeStatus AND m.isDeleted=false ORDER BY m.name")
	List<MemberEntity> findMembersByActiveStatus(@Param("activeStatus") ActiveStatus activeStatus);

	@Query("SELECT m FROM MemberEntity m WHERE m.isDeleted=false ORDER BY m.name")
	List<MemberEntity> findMembers();

	@Query("SELECT m FROM MemberEntity  m WHERE m.id IN :ids AND m.isDeleted=false ORDER BY m.name")
	List<MemberEntity> findMembersByIds(@Param("ids") List<Long> ids);

	@Query("SELECT m.name FROM MemberEntity  m WHERE m.id=:id AND m.isDeleted=false")
	Optional<String> findNameById(@Param("id") Long id);

	@Query(
			"SELECT m FROM MemberEntity m WHERE m.oauthServerType = :oauthServerType AND m.isDeleted=false"
					+ " AND m.id NOT IN (SELECT a.memberId FROM AccountEntity a WHERE a.isDeleted=false)")
	List<MemberEntity> findSlackOnlyMembers(
			@Param("oauthServerType") OauthServerType oauthServerType);

	// 동작하지 않는 쿼리 : SQL 의 FILED 함수 내에 List<Long> 이 들어갈 때, 단일 파라미터로 들어가기 때문에 CustomRepository 에서 동적으로
	// 쿼리를 생성해서 처리
	//	@Query("SELECT m FROM MemberEntity m WHERE m.id IN :ids AND m.isDeleted=false ORDER BY
	// FUNCTION('FIELD', m.id, :ids) ")
	//	List<MemberEntity> findMembersByIdsInOrder(@Param("ids") List<Long> ids);

}
