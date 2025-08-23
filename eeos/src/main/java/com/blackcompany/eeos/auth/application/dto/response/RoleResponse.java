package com.blackcompany.eeos.auth.application.dto.response;

import com.blackcompany.eeos.auth.application.model.Role;
import com.blackcompany.eeos.common.support.dto.AbstractResponseDto;

public record RoleResponse(
        Long roleId,
        String name
) implements AbstractResponseDto {

    public static RoleResponse from(Role role){
        return new RoleResponse(role.getId(), role.getRole());
    }

}
