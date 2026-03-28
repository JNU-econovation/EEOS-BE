package com.blackcompany.eeos.slackEvent.infra.client;

import com.blackcompany.eeos.slackEvent.application.dto.EeosSlackMessageForwardRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
		name = "EeosSlackForwardOpenFeign",
		url = "${slack.event.forward.base-url:http://localhost:8080}")
public interface EeosSlackForwardApiClientImpl extends EeosSlackForwardApiClient {

	@Override
	@PostMapping(
			path = "${slack.event.forward.path:/api/internal/slack/announcements}",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	void forward(
			@RequestHeader(value = "X-EEOS-API-KEY", required = false) String apiKey,
			@RequestBody EeosSlackMessageForwardRequest request);
}
