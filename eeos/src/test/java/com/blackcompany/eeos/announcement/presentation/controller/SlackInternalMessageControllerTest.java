package com.blackcompany.eeos.announcement.presentation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blackcompany.eeos.announcement.application.usecase.SaveAnnouncementUsecase;
import com.blackcompany.eeos.auth.application.domain.token.TokenResolver;
import com.blackcompany.eeos.auth.presentation.support.TokenExtractor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(SlackInternalMessageController.class)
@AutoConfigureMockMvc(addFilters = false)
class SlackInternalMessageControllerTest {

	@Autowired private MockMvc mockMvc;

	@MockBean private SaveAnnouncementUsecase saveAnnouncementUsecase;
	@MockBean private TokenResolver tokenResolver;

	@MockBean(name = "header")
	private TokenExtractor headerTokenExtractor;

	@MockBean(name = "cookie")
	private TokenExtractor cookieTokenExtractor;

	@Test
	@DisplayName("유효한 요청으로 POST 요청을 보내면 201 Created를 반환한다")
	void post_with_valid_request_returns_201() throws Exception {
		String body =
				"""
				{
					"eventId": "Ev-100",
					"teamId": "T123",
					"channelId": "C123",
					"userId": "U123",
					"text": "[공지] 안녕하세요",
					"messageTs": "1710000000.000100",
					"threadTs": null
				}
				""";
		doNothing().when(saveAnnouncementUsecase).save(any());

		mockMvc
				.perform(
						post("/api/internal/slack/messages")
								.contentType(MediaType.APPLICATION_JSON)
								.content(body))
				.andExpect(status().isCreated());
	}
}
