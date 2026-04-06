package com.blackcompany.eeos.program.infra.api.slack.chat.client;

import com.blackcompany.eeos.program.infra.api.slack.chat.dto.SlackChatPostMessageResponse;
import com.blackcompany.eeos.program.infra.api.slack.chat.dto.SlackConversationOpenResponse;

public interface SlackChatApiClient {

	SlackConversationOpenResponse openConversation(final String token, final String users);

	SlackChatPostMessageResponse post(
			final String token, final String channel, final String blocks, final String username);
}
