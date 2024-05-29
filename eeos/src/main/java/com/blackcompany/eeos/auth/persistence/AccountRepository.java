package com.blackcompany.eeos.auth.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    @Query(
        "SELECT a.memberId FROM AccountEntity a WHERE a.loginId = :loginId AND a.passWd = :passWd")
    List<Long> findByMemberId(
            @Param("loginId") String loginId, @Param("passWd") String passWd);

}
