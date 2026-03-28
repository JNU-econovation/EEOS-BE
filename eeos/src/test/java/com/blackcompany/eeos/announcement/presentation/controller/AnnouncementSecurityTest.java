package com.blackcompany.eeos.announcement.presentation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blackcompany.eeos.announcement.application.usecase.GetAnnouncementsUsecase;
import com.blackcompany.eeos.auth.application.domain.token.TokenResolver;
import com.blackcompany.eeos.auth.application.exception.NotFoundHeaderTokenException;
import com.blackcompany.eeos.auth.presentation.support.TokenExtractor;
import com.blackcompany.eeos.config.security.AccessTokenEntryPoint;
import com.blackcompany.eeos.config.security.DynamicCorsConfigurationSource;
import com.blackcompany.eeos.config.security.SecurityFilterChainConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AnnouncementController.class)
@Import({SecurityFilterChainConfig.class, AccessTokenEntryPoint.class})
@TestPropertySource(properties = "eeos.internal-api-key=test-key")
class AnnouncementSecurityTest {

	@Autowired private MockMvc mockMvc;

	@MockBean private GetAnnouncementsUsecase getAnnouncementsUsecase;
	@MockBean private TokenResolver tokenResolver;

	@MockBean(name = "header")
	private TokenExtractor headerTokenExtractor;

	@MockBean(name = "cookie")
	private TokenExtractor cookieTokenExtractor;

	@MockBean private DynamicCorsConfigurationSource corsConfigurationSource;

	@BeforeEach
	void setup() {
		when(headerTokenExtractor.extract(any())).thenThrow(NotFoundHeaderTokenException.class);
	}

	@Test
	@DisplayName("인증 없이 GET /api/announcements 요청 시 401 Unauthorized를 반환한다")
	void get_announcements_without_authentication_returns_401() throws Exception {
		mockMvc.perform(get("/api/announcements")).andExpect(status().isUnauthorized());
	}
}
