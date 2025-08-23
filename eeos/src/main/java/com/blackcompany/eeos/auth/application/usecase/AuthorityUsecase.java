package com.blackcompany.eeos.auth.application.usecase;

import com.blackcompany.eeos.auth.application.model.Role;
import java.util.List;

public interface AuthorityUsecase {

    void changeRole(Long memberId, String from, String to);

    List<Role> getOrganizationRoles();

}
