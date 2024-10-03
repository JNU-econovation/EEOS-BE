package com.blackcompany.eeos.team.persistence;

import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface TeamRepository extends JpaRepository<TeamEntity, Long> {

	@Query("SELECT T FROM TeamEntity T WHERE T.name =:teamName")
	List<TeamEntity> findTeamEntityByName(@Param("teamName") String teamName);

	@Modifying
	@Transactional
	@Query("UPDATE TeamEntity T SET T.status=false where T.id=:teamId")
	void deleteTeamEntityByName(@Param("teamId") Long teamId);

	@Query("SELECT T FROM TeamEntity T WHERE T.status = true order by T.name")
	List<TeamEntity> findAllActiveTeams();


	@Query("SELECT T FROM TeamEntity T ORDER BY T.name")
	List<TeamEntity> findAllTeams();
}
