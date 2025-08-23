package com.blackcompany.eeos.auth.application.dto.request;

import com.blackcompany.eeos.auth.application.model.Role;
import com.blackcompany.eeos.common.support.dto.AbstractRequestDto;
import jakarta.validation.constraints.AssertTrue;

public record AuthorityUpdateRequest(
        Long memberId,
        String from,
        String to
) implements AbstractRequestDto {

    @AssertTrue(message = "전달받은 role 이 존재하지 않습니다.")
    public boolean existsRole(){
        return Role.isExist(from)
                && Role.isExist(to);
    }

}
