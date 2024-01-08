package com.blackcompany.eeos.member.persistence;

import com.blackcompany.eeos.member.application.model.ActiveStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

	@Query(
			"SELECT m FROM MemberEntity m inner join AttendEntity a on m.id = a.memberId where a.programId = :programId AND m.isDeleted=false ORDER BY  m.name")
	List<MemberEntity> findMembersByProgramId(@Param("programId") Long programId);

	@Query(
			"SELECT m FROM MemberEntity m where m.activeStatus= :activeStatus AND m.isDeleted=false ORDER BY m.name")
	List<MemberEntity> findMembersByActiveStatus(@Param("activeStatus") ActiveStatus activeStatus);

	@Query("SELECT m FROM MemberEntity m WHERE m.isDeleted=false ORDER BY m.name")
	List<MemberEntity> findMembers();

	@Query("SELECT m FROM MemberEntity  m WHERE m.isDeleted=false AND m.id IN :ids")
	List<MemberEntity> findMembersByIds(@Param("ids") List<Long> ids);
}
