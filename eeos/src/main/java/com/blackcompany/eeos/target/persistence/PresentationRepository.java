package com.blackcompany.eeos.target.persistence;

import com.blackcompany.eeos.team.persistence.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PresentationRepository extends JpaRepository<PresentationEntity, Long> {

    @Query("SELECT t FROM TeamEntity t WHERE t.id in (SELECT p.id FROM PresentationEntity p WHERE p.programId=:programId)")
    List<TeamEntity> findTeamsByProgramId(@Param("programId") Long programId);

}
