package com.blackcompany.eeos.auth.presentation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.blackcompany.eeos.auth.application.domain.TokenModel;
import com.blackcompany.eeos.auth.application.dto.converter.TokenResponseConverter;
import com.blackcompany.eeos.auth.application.usecase.LoginUsecase;
import com.blackcompany.eeos.auth.presentation.support.AuthCookieManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class V1LoginControllerTest {

	MockMvc mockMvc;
	ObjectMapper objectMapper = new ObjectMapper();

	@Mock LoginUsecase loginUsecase;
	@Mock AuthCookieManager cookieManager;
	@Spy TokenResponseConverter tokenResponseConverter;
	@InjectMocks V1LoginController v1LoginController;

	private final TokenModel tokenModel =
			TokenModel.builder()
					.accessToken("test-at")
					.refreshToken("test-rt")
					.accessExpiredTime(9999999L)
					.refreshExpiredTime(9999999L)
					.build();

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(v1LoginController).build();
	}

	@Test
	@DisplayName("WEB: Client-Type 헤더가 WEB이면 AT만 body에 반환하고 refreshToken은 없다")
	void shouldNotIncludeRefreshTokenInBodyWhenWebClientType() throws Exception {
		given(loginUsecase.login(eq("testuser"), eq("test1234"))).willReturn(tokenModel);
		given(cookieManager.setCookie(any(), any()))
				.willReturn(ResponseCookie.from("eeos_token", "test-rt").build());

		mockMvc
				.perform(
						post("/api/v1/auth/login")
								.header("Client-Type", "WEB")
								.contentType(MediaType.APPLICATION_JSON)
								.content(
										objectMapper.writeValueAsString(
												Map.of("id", "testuser", "password", "test1234"))))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.data.accessToken").value("test-at"))
				.andExpect(jsonPath("$.data.refreshToken").doesNotExist());
	}

	@Test
	@DisplayName("APP: Client-Type 헤더가 APP이면 AT+RT 모두 body에 반환한다")
	void shouldReturnBothTokensInBodyWhenAppClientType() throws Exception {
		given(loginUsecase.login(eq("testuser"), eq("test1234"))).willReturn(tokenModel);

		mockMvc
				.perform(
						post("/api/v1/auth/login")
								.header("Client-Type", "APP")
								.contentType(MediaType.APPLICATION_JSON)
								.content(
										objectMapper.writeValueAsString(
												Map.of("id", "testuser", "password", "test1234"))))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.data.accessToken").value("test-at"))
				.andExpect(jsonPath("$.data.refreshToken").value("test-rt"));
	}

	@Test
	@DisplayName("Client-Type 헤더가 없으면 WEB으로 처리한다")
	void shouldDefaultToWebWhenNoClientTypeHeader() throws Exception {
		given(loginUsecase.login(any(), any())).willReturn(tokenModel);
		given(cookieManager.setCookie(any(), any()))
				.willReturn(ResponseCookie.from("eeos_token", "test-rt").build());

		mockMvc
				.perform(
						post("/api/v1/auth/login")
								.contentType(MediaType.APPLICATION_JSON)
								.content(
										objectMapper.writeValueAsString(
												Map.of("id", "testuser", "password", "test1234"))))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.data.refreshToken").doesNotExist());
	}
}
