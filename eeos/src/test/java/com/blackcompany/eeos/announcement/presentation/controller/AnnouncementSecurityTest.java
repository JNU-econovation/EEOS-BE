package com.blackcompany.eeos.announcement.presentation.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blackcompany.eeos.auth.application.domain.token.TokenProvider;
import com.blackcompany.eeos.auth.application.domain.token.TokenResolver;
import com.blackcompany.eeos.auth.application.service.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("local")
class AnnouncementSecurityTest {

	@Autowired private MockMvc mockMvc;

	@MockBean private TokenProvider tokenProvider;
	@MockBean private TokenResolver tokenResolver;
	@MockBean private AuthService authService;

	@Test
	@DisplayName("인증 없이 GET /api/announcements 요청 시 401 Unauthorized를 반환한다")
	void get_announcements_without_authentication_returns_401() throws Exception {
		// given - 인증 헤더 없이 요청

		// when & then
		mockMvc.perform(get("/api/announcements")).andExpect(status().isUnauthorized());
	}
}
