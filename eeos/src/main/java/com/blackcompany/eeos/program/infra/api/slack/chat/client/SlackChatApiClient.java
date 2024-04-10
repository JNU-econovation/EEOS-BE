package com.blackcompany.eeos.program.infra.api.slack.chat.client;

import com.blackcompany.eeos.program.infra.api.slack.chat.dto.SlackChatPostMessageResponse;
import com.blackcompany.eeos.program.infra.api.slack.chat.model.ChatPostModel;
import com.sun.xml.bind.v2.runtime.output.Encoded;

public interface SlackChatApiClient {

	SlackChatPostMessageResponse post(final String channel,
										  final String blocks,
										  final String username,
										  final String pretty);
}
