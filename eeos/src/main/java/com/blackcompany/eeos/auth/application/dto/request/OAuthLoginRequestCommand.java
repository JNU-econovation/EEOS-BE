package com.blackcompany.eeos.auth.application.dto.request;

import com.blackcompany.eeos.common.support.dto.AbstractApplicationDto;

public record OAuthLoginRequestCommand(String oauthServerType, String code, String uri)
		implements AbstractApplicationDto {}
