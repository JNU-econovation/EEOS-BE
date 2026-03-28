package com.blackcompany.eeos.slackEvent.infra.client;

import com.blackcompany.eeos.slackEvent.application.dto.EeosSlackMessageForwardRequest;

public interface EeosSlackForwardApiClient {

	void forward(String apiKey, EeosSlackMessageForwardRequest request);
}
