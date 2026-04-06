package com.blackcompany.eeos.auth.application.support;

import com.blackcompany.eeos.auth.application.domain.TokenModel;
import com.blackcompany.eeos.auth.application.domain.converter.TokenModelConverter;
import com.blackcompany.eeos.auth.application.domain.token.TokenProvider;
import com.blackcompany.eeos.auth.application.domain.token.TokenResolver;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationTokenGenerator {
	private final TokenProvider tokenProvider;
	private final TokenModelConverter tokenModelConverter;
	private final TokenResolver tokenResolver;

	public TokenModel execute(final Long memberId) {
		String accessToken = tokenProvider.createAccessToken(memberId);
		String refreshToken = tokenProvider.createRefreshToken(memberId);

		return tokenModelConverter.from(
				accessToken,
				tokenResolver.getExpiredDateByAccessToken(accessToken),
				refreshToken,
				tokenResolver.getExpiredDateByRefreshToken(refreshToken));
	}

	public TokenModel execute(final Long memberId, final String clientType, final String clientId) {
		String accessToken = tokenProvider.createAccessToken(memberId);
		String refreshToken = tokenProvider.createRefreshToken(memberId, clientType, clientId);

		return tokenModelConverter.from(
				accessToken,
				tokenResolver.getExpiredDateByAccessToken(accessToken),
				refreshToken,
				tokenResolver.getExpiredDateByRefreshToken(refreshToken));
	}

	public TokenModel execute(final Long memberId, Set<String> authorities) {
		String accessToken = tokenProvider.createAccessToken(memberId, authorities);
		String refreshToken = tokenProvider.createRefreshToken(memberId, authorities);

		return tokenModelConverter.from(
				accessToken,
				tokenResolver.getExpiredDateByAccessToken(accessToken),
				refreshToken,
				tokenResolver.getExpiredDateByRefreshToken(refreshToken));
	}
}
