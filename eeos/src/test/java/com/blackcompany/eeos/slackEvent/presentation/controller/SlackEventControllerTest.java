package com.blackcompany.eeos.slackEvent.presentation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.doNothing;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blackcompany.eeos.auth.application.domain.token.TokenResolver;
import com.blackcompany.eeos.auth.presentation.support.TokenExtractor;
import com.blackcompany.eeos.slackEvent.application.dto.SlackEventAckResponse;
import com.blackcompany.eeos.slackEvent.application.support.SlackRequestSignatureVerifier;
import com.blackcompany.eeos.slackEvent.application.usecase.HandleSlackEventUsecase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(SlackEventController.class)
@AutoConfigureMockMvc(addFilters = false)
class SlackEventControllerTest {

	@Autowired private MockMvc mockMvc;

	@MockBean private HandleSlackEventUsecase handleSlackEventUsecase;
	@MockBean private SlackRequestSignatureVerifier slackRequestSignatureVerifier;
	@MockBean private TokenResolver tokenResolver;

	@MockBean(name = "header")
	private TokenExtractor headerTokenExtractor;

	@MockBean(name = "cookie")
	private TokenExtractor cookieTokenExtractor;

	@Test
	@DisplayName("url_verification 요청이면 challenge를 그대로 반환한다")
	void url_verification_returns_challenge() throws Exception {
		// given
		String body = "{\"type\":\"url_verification\",\"challenge\":\"challenge-value\"}";
		String timestamp = "1710000000";
		String signature = "v0=test-signature";
		doNothing().when(slackRequestSignatureVerifier).verify(timestamp, signature, body);

		// when & then
		mockMvc
				.perform(
						post("/api/slack/events")
								.contentType(MediaType.APPLICATION_JSON)
								.header("X-Slack-Request-Timestamp", timestamp)
								.header("X-Slack-Signature", signature)
								.content(body))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.challenge").value("challenge-value"));

		then(slackRequestSignatureVerifier).should().verify(timestamp, signature, body);
		verifyNoInteractions(handleSlackEventUsecase);
	}

	@Test
	@DisplayName("event_callback 요청이면 처리 결과 ACK를 반환한다")
	void event_callback_returns_ack() throws Exception {
		// given
		String body =
				"""
				{
					"type": "event_callback",
					"event_id": "Ev-100",
					"event_time": 1710000000,
					"team_id": "T123",
					"event": {
						"type": "message",
						"channel": "C123",
						"user": "U123",
						"text": "안녕하세요",
						"ts": "1710000000.000100",
						"thread_ts": "1710000000.000100",
						"subtype": null
					}
				}
				""";
		String timestamp = "1710000000";
		String signature = "v0=test-signature";
		doNothing().when(slackRequestSignatureVerifier).verify(timestamp, signature, body);
		given(handleSlackEventUsecase.handle(any()))
				.willReturn(
						SlackEventAckResponse.builder()
								.accepted(true)
								.eventId("Ev-100")
								.status("FORWARDED")
								.build());

		// when & then
		mockMvc
				.perform(
						post("/api/slack/events")
								.contentType(MediaType.APPLICATION_JSON)
								.header("X-Slack-Request-Timestamp", timestamp)
								.header("X-Slack-Signature", signature)
								.content(body))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.accepted").value(true))
				.andExpect(jsonPath("$.eventId").value("Ev-100"))
				.andExpect(jsonPath("$.status").value("FORWARDED"));

		then(slackRequestSignatureVerifier).should().verify(timestamp, signature, body);
		then(handleSlackEventUsecase).should().handle(any());
	}

	@Test
	@DisplayName("지원하지 않는 이벤트 타입이면 400을 반환한다")
	void unsupported_type_returns_bad_request() throws Exception {
		// given
		String body = "{\"type\":\"unknown_event_type\"}";
		String timestamp = "1710000000";
		String signature = "v0=test-signature";
		doNothing().when(slackRequestSignatureVerifier).verify(timestamp, signature, body);

		// when & then
		mockMvc
				.perform(
						post("/api/slack/events")
								.contentType(MediaType.APPLICATION_JSON)
								.header("X-Slack-Request-Timestamp", timestamp)
								.header("X-Slack-Signature", signature)
								.content(body))
				.andExpect(status().isBadRequest());

		then(slackRequestSignatureVerifier).should().verify(timestamp, signature, body);
		verifyNoInteractions(handleSlackEventUsecase);
	}
}
