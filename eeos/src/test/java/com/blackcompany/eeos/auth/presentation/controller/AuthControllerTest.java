package com.blackcompany.eeos.auth.presentation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blackcompany.eeos.auth.application.domain.TokenModel;
import com.blackcompany.eeos.auth.application.domain.token.TokenResolver;
import com.blackcompany.eeos.auth.application.dto.converter.TokenResponseConverter;
import com.blackcompany.eeos.auth.application.dto.request.EeosSignUpCommand;
import com.blackcompany.eeos.auth.application.usecase.EeosSignUpUseCase;
import com.blackcompany.eeos.auth.application.usecase.LogOutUsecase;
import com.blackcompany.eeos.auth.application.usecase.LoginUsecase;
import com.blackcompany.eeos.auth.application.usecase.OAuthSignUpUseCase;
import com.blackcompany.eeos.auth.application.usecase.ReissueUsecase;
import com.blackcompany.eeos.auth.application.usecase.WithDrawUsecase;
import com.blackcompany.eeos.auth.presentation.support.AuthCookieManager;
import com.blackcompany.eeos.auth.presentation.support.TokenExtractor;
import com.blackcompany.eeos.common.presentation.support.CookieManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

	private MockMvc mockMvc;
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Mock private LoginUsecase loginUsecase;
	@Mock private ReissueUsecase reissueUsecase;
	@Mock private TokenExtractor tokenExtractor;
	@Mock private CookieManager cookieManager;
	@Mock private AuthCookieManager authCookieManager;
	@Spy private TokenResponseConverter tokenResponseConverter;
	@Mock private LogOutUsecase logOutUsecase;
	@Mock private WithDrawUsecase withDrawUsecase;
	@Mock private OAuthSignUpUseCase oAuthSignUpUseCase;
	@Mock private EeosSignUpUseCase eeosSignUpUseCase;
	@Mock private TokenResolver tokenResolver;

	private AuthController authController;

	private final TokenModel tokenModel =
			TokenModel.builder()
					.accessToken("test-at")
					.refreshToken("test-rt")
					.accessExpiredTime(9999999L)
					.refreshExpiredTime(19999999L)
					.build();

	@BeforeEach
	void setUp() {
		authController =
				new AuthController(
						loginUsecase,
						reissueUsecase,
						tokenExtractor,
						tokenResponseConverter,
						cookieManager,
						authCookieManager,
						logOutUsecase,
						withDrawUsecase,
						oAuthSignUpUseCase,
						eeosSignUpUseCase,
						tokenResolver);
		mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
	}

	@Test
	@DisplayName("slackMemberId가 없으면 일반 회원가입 유스케이스를 호출한다.")
	void signUp_withoutSlackMemberId_usesEeosSignUpUseCase() throws Exception {
		when(eeosSignUpUseCase.signUp(any(EeosSignUpCommand.class))).thenReturn(tokenModel);
		when(cookieManager.setCookie(any(), any()))
				.thenReturn(ResponseCookie.from("eeos_token", "test-rt").path("/").build());

		mockMvc
				.perform(
						post("/api/auth/signup")
								.contentType(MediaType.APPLICATION_JSON)
								.content(
										objectMapper.writeValueAsString(
												Map.of(
														"id", "testuser",
														"password", "test1234!",
														"generation", 30,
														"name", "홍길동",
														"activeStatus", "am"))))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.data.accessToken").value("test-at"))
				.andExpect(header().string("Set-Cookie", org.hamcrest.Matchers.containsString("test-rt")));

		verify(eeosSignUpUseCase).signUp(any(EeosSignUpCommand.class));
		verify(eeosSignUpUseCase, never()).signUp(any(EeosSignUpCommand.class), any());
	}

	@Test
	@DisplayName("slackMemberId가 있으면 동일 가입 유스케이스의 Slack 연동 메소드를 호출한다.")
	void signUp_withSlackMemberId_usesEeosSignUpUseCaseOverload() throws Exception {
		when(eeosSignUpUseCase.signUp(any(EeosSignUpCommand.class), eq("U08ABCDE123")))
				.thenReturn(tokenModel);
		when(cookieManager.setCookie(any(), any()))
				.thenReturn(ResponseCookie.from("eeos_token", "test-rt").path("/").build());

		mockMvc
				.perform(
						post("/api/auth/signup")
								.param("code", "U08ABCDE123")
								.contentType(MediaType.APPLICATION_JSON)
								.content(
										objectMapper.writeValueAsString(
												Map.of(
														"id", "testuser",
														"password", "test1234!",
														"generation", 30,
														"name", "홍길동",
														"activeStatus", "am"))))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.data.accessToken").value("test-at"))
				.andExpect(header().string("Set-Cookie", org.hamcrest.Matchers.containsString("test-rt")));

		verify(eeosSignUpUseCase).signUp(any(EeosSignUpCommand.class), eq("U08ABCDE123"));
		verify(eeosSignUpUseCase, never()).signUp(any(EeosSignUpCommand.class));
	}
}
