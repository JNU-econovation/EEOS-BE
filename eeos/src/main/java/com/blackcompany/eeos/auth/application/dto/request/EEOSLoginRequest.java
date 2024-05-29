package com.blackcompany.eeos.auth.application.dto.request;

import com.blackcompany.eeos.common.support.dto.AbstractDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class EEOSLoginRequest implements AbstractDto {

    private final String loginId;

    private final String password;

}
