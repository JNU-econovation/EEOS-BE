package com.blackcompany.eeos.auth.application.domain.converter;

import com.blackcompany.eeos.auth.application.domain.TokenModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenModelConverter {
	public TokenModel from(
			String accessToken, Long accessExpiredTime, String refreshToken, Long refreshExpiredTime) {
		return TokenModel.builder()
				.accessToken(accessToken)
				.accessExpiredTime(accessExpiredTime)
				.refreshToken(refreshToken)
				.refreshExpiredTime(refreshExpiredTime)
				.build();
	}
}
