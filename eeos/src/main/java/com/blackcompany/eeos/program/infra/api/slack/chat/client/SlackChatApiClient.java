package com.blackcompany.eeos.program.infra.api.slack.chat.client;

import com.blackcompany.eeos.program.infra.api.slack.chat.dto.SlackChatPostMessageResponse;
import com.blackcompany.eeos.program.infra.api.slack.chat.model.ChatPostModel;

public interface SlackChatApiClient{

    <T> SlackChatPostMessageResponse post(ChatPostModel model);

}
