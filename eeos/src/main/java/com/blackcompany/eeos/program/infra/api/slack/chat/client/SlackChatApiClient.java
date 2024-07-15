package com.blackcompany.eeos.program.infra.api.slack.chat.client;

import com.blackcompany.eeos.program.infra.api.slack.chat.dto.SlackChatPostMessageResponse;

public interface SlackChatApiClient {

	SlackChatPostMessageResponse post(
			final String token, final String channel, final String blocks, final String username);
}
