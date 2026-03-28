package com.blackcompany.eeos.slackEvent.presentation.controller;

import com.blackcompany.eeos.slackEvent.application.dto.SlackEventAckResponse;
import com.blackcompany.eeos.slackEvent.application.dto.SlackEventEnvelopeRequest;
import com.blackcompany.eeos.slackEvent.application.dto.SlackUrlVerificationResponse;
import com.blackcompany.eeos.slackEvent.application.exception.SlackEventParsingException;
import com.blackcompany.eeos.slackEvent.application.exception.UnsupportedSlackEventTypeException;
import com.blackcompany.eeos.slackEvent.application.support.SlackRequestSignatureVerifier;
import com.blackcompany.eeos.slackEvent.application.usecase.HandleSlackEventUsecase;
import com.blackcompany.eeos.slackEvent.presentation.docs.SlackEventApi;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/slack")
@RequiredArgsConstructor
public class SlackEventController implements SlackEventApi {

	private static final String URL_VERIFICATION = "url_verification";
	private static final String EVENT_CALLBACK = "event_callback";

	private final SlackRequestSignatureVerifier slackRequestSignatureVerifier;
	private final HandleSlackEventUsecase handleSlackEventUsecase;
	private final ObjectMapper objectMapper;

	@Override
	@PostMapping("/events")
	public ResponseEntity<?> receive(
			@RequestHeader("X-Slack-Request-Timestamp") String requestTimestamp,
			@RequestHeader("X-Slack-Signature") String requestSignature,
			@RequestBody String rawBody) {

		// 이 엔드포인트는 public 이므로, slack 에서 보낸 게 맞는지 검증
		slackRequestSignatureVerifier.verify(requestTimestamp, requestSignature, rawBody);

		// Request Parsing
		SlackEventEnvelopeRequest request = parse(rawBody);

		// 요청 타입이 URL 검증일 경우, 받은 challenge를 반환
		if (URL_VERIFICATION.equals(request.getType())) {
			return urlVerification(request.getChallenge());
		}

		// 요청 타입이 이벤트 콜백일 경우, 이벤트 처리
		if (EVENT_CALLBACK.equals(request.getType())) {
			log.info("Event callback received");
			SlackEventAckResponse response = handleSlackEventUsecase.handle(request);
			return eventCallback(response);
		}

		// 이외의 타입인 경우 에러 throw
		throw new UnsupportedSlackEventTypeException(request.getType());
	}

	private ResponseEntity<SlackUrlVerificationResponse> urlVerification(String challenge) {
		return ResponseEntity.ok(new SlackUrlVerificationResponse(challenge));
	}

	private ResponseEntity<SlackEventAckResponse> eventCallback(SlackEventAckResponse response) {
		return ResponseEntity.ok(response);
	}

	private SlackEventEnvelopeRequest parse(String rawBody) {
		try {
			return objectMapper.readValue(rawBody, SlackEventEnvelopeRequest.class);
		} catch (Exception e) {
			throw new SlackEventParsingException();
		}
	}
}
