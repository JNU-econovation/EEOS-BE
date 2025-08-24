package com.blackcompany.eeos.auth.application.repository;

import com.blackcompany.eeos.auth.application.model.AuthorityModel;
import com.blackcompany.eeos.auth.application.model.Role;
import java.util.Set;

public interface AuthorityRepository {

	Set<AuthorityModel> findByMemberId(Long memberId);

	AuthorityModel findByMemberIdAndRole(Long memberId, Role role);

	Long save(AuthorityModel authorityModel);
}
