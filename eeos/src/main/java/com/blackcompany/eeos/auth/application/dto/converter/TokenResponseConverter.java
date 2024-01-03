package com.blackcompany.eeos.auth.application.dto.converter;

import com.blackcompany.eeos.auth.application.dto.response.TokenResponse;
import org.springframework.stereotype.Component;

@Component
public class TokenResponseConverter {
	public TokenResponse from(String accessToken, Long accessExpiredTime) {
		return TokenResponse.builder()
				.accessToken(accessToken)
				.accessExpiredTime(accessExpiredTime)
				.build();
	}
}
