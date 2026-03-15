package com.blackcompany.eeos.slackEvent.presentation.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Slack Event", description = "Slack Events API 웹훅 수신 API")
public interface SlackEventApi {

	@Operation(summary = "Slack 이벤트 수신", description = "Slack Events API 요청을 검증하고 처리한다.")
	ResponseEntity<?> receive(
			@Parameter(description = "Slack 요청 타임스탬프") String requestTimestamp,
			@Parameter(description = "Slack 요청 서명") String requestSignature,
			String rawBody);
}
