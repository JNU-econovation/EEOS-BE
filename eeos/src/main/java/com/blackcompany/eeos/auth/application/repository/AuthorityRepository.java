package com.blackcompany.eeos.auth.application.repository;

import com.blackcompany.eeos.auth.application.model.AuthorityModel;
import java.util.Set;

public interface AuthorityRepository {

    Set<AuthorityModel> findByMemberId(Long memberId);

}
