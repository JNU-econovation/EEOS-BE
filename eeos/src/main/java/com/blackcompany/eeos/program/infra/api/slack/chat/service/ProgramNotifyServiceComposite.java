package com.blackcompany.eeos.program.infra.api.slack.chat.service;

import com.blackcompany.eeos.program.application.dto.CommandProgramResponse;
import com.blackcompany.eeos.program.application.dto.converter.ProgramResponseConverter;
import com.blackcompany.eeos.program.application.model.ProgramNotificationModel;
import com.blackcompany.eeos.program.infra.api.slack.chat.client.SlackChatApiClient;
import com.blackcompany.eeos.program.infra.api.slack.chat.dto.SlackChatPostMessageResponse;
import com.blackcompany.eeos.program.infra.api.slack.chat.exception.SlackChatApiUnAuthException;
import com.blackcompany.eeos.program.infra.api.slack.chat.mapper.ObjectToJsonMapper;
import com.blackcompany.eeos.program.infra.api.slack.chat.model.BotTokens;
import com.blackcompany.eeos.program.infra.api.slack.chat.model.Channels;
import com.blackcompany.eeos.program.infra.api.slack.chat.model.ChatPostModel;
import com.blackcompany.eeos.program.infra.api.slack.chat.model.converter.ChatPostModelConverter;
import com.blackcompany.eeos.program.infra.api.slack.chat.support.ChannelGetter;
import com.blackcompany.eeos.program.infra.api.slack.chat.support.TokenGetter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProgramNotifyServiceComposite {

	private final SlackChatApiClient client;
	private final ChatPostModelConverter modelConverter;
	private final ProgramResponseConverter responseConverter;
	private final Channels channels;
	private final BotTokens tokens;
	private final ObjectToJsonMapper<ChatPostModel.Block[]> mapper;

	private final String BEARER = "Bearer";

	public CommandProgramResponse notify(ProgramNotificationModel notiModel) {

		ChatPostModel model = modelConverter.from(notiModel);
		SlackChatPostMessageResponse response =
				post(getBlackCompanyBotToken(), getBlackCompanyNotiChannel(), model);

		if (!response.isOk()) throw new SlackChatApiUnAuthException(response.getError());

		return responseConverter.from(notiModel.getId());
	}

	private TokenGetter getEconoBotToken() {
		return () -> String.format("%s %s", BEARER, tokens.getECONOVATION_EEOS_BOT());
	}

	private TokenGetter getBlackCompanyBotToken() {
		return () -> String.format("%s %s", BEARER, tokens.getBLACK_COMPANY_EEOS_BOT());
	}

	private ChannelGetter getBlackCompanyNotiChannel() {
		return () -> channels.getBLACK_COMPANY_NOTIFICATION();
	}

	private ChannelGetter getNotiChannel() {
		return () -> channels.getECONOVATION_NOTIFICATION();
	}

	private ChannelGetter getSmallTalkChannel() {
		return () -> channels.getECONOVATION_SMALL_TALK();
	}

	private ChannelGetter getTestChannel() {
		return () -> channels.getBLACK_COMPANY_TEST();
	}

	private SlackChatPostMessageResponse post(
			TokenGetter token, ChannelGetter channel, ChatPostModel model) {
		return client.post(
				token.getToken(),
				channel.getChannel(),
				mapper.toJson(model.getMessage()),
				model.getUserName());
	}
}
