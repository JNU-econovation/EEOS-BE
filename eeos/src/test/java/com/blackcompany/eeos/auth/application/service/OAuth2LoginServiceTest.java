package com.blackcompany.eeos.auth.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.blackcompany.eeos.auth.application.domain.AuthorizationCodeData;
import com.blackcompany.eeos.auth.application.domain.ClientType;
import com.blackcompany.eeos.auth.application.domain.TokenModel;
import com.blackcompany.eeos.auth.application.support.AuthenticationTokenGenerator;
import com.blackcompany.eeos.auth.application.support.LoginRateLimiter;
import com.blackcompany.eeos.auth.persistence.AuthorizationCodeRepository;
import com.blackcompany.eeos.auth.persistence.client.ClientEntity;
import com.blackcompany.eeos.member.application.model.MemberModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OAuth2LoginServiceTest {

	@Mock AuthService authService;
	@Mock ClientService clientService;
	@Mock AuthenticationTokenGenerator tokenGenerator;
	@Mock AuthorizationCodeRepository codeRepository;
	@Mock LoginRateLimiter rateLimiter;
	@InjectMocks OAuth2LoginService oAuth2LoginService;

	@Test
	@DisplayName("should return TokenModel for WEB client login")
	void shouldReturnTokenModelForWebLogin() {
		// Given
		ClientEntity client =
				ClientEntity.builder().clientId("web-client-id").clientType(ClientType.WEB).build();
		client.addRedirectUri("https://eeos.econovation.kr/callback");

		when(clientService.findAndValidateRedirectUri(
						"web-client-id", "https://eeos.econovation.kr/callback"))
				.thenReturn(client);

		MemberModel memberModel = MemberModel.builder().name("test").build();
		// MemberModel.id는 private이므로 reflection 없이 getMemberId()를 mock할 수 없음
		// 대신 AllArgsConstructor를 사용
		MemberModel member = new MemberModel(1L, "test", null, false, null, null);
		when(authService.authenticate("user@test.com", "password")).thenReturn(member);

		when(tokenGenerator.execute(1L, "WEB", "web-client-id"))
				.thenReturn(
						TokenModel.builder()
								.accessToken("at")
								.refreshToken("rt")
								.accessExpiredTime(100L)
								.refreshExpiredTime(200L)
								.build());

		// When
		TokenModel result =
				oAuth2LoginService.loginForWeb(
						"web-client-id",
						"https://eeos.econovation.kr/callback",
						"user@test.com",
						"password",
						"127.0.0.1");

		// Then
		assertNotNull(result.getAccessToken());
		verify(rateLimiter).resetAccountCounter("user@test.com");
	}

	@Test
	@DisplayName("should return authorization code for APP client login")
	void shouldReturnAuthorizationCodeForAppLogin() {
		// Given
		ClientEntity client =
				ClientEntity.builder().clientId("app-client-id").clientType(ClientType.APP).build();
		client.addRedirectUri("kr.econovation.eeos://callback");

		when(clientService.findAndValidateRedirectUri(
						"app-client-id", "kr.econovation.eeos://callback"))
				.thenReturn(client);

		MemberModel member = new MemberModel(2L, "test", null, false, null, null);
		when(authService.authenticate("user@test.com", "password")).thenReturn(member);

		when(codeRepository.save(any(AuthorizationCodeData.class))).thenReturn("generated-code");

		// When
		String code =
				oAuth2LoginService.loginForApp(
						"app-client-id",
						"kr.econovation.eeos://callback",
						"user@test.com",
						"password",
						"127.0.0.1",
						"code-challenge-value",
						"S256");

		// Then
		assertEquals("generated-code", code);
		verify(rateLimiter).resetAccountCounter("user@test.com");
	}
}
