package com.blackcompany.eeos.program.infra.api.slack.chat.service;

import com.blackcompany.eeos.program.application.dto.CommandProgramResponse;
import com.blackcompany.eeos.program.application.dto.converter.ProgramResponseConverter;
import com.blackcompany.eeos.program.application.model.ProgramNotificationModel;
import com.blackcompany.eeos.program.infra.api.slack.chat.client.SlackChatApiClient;
import com.blackcompany.eeos.program.infra.api.slack.chat.dto.SlackChatPostMessageResponse;
import com.blackcompany.eeos.program.infra.api.slack.chat.exception.SlackChatApiUnAuthException;
import com.blackcompany.eeos.program.infra.api.slack.chat.mapper.ObjectToJsonMapper;
import com.blackcompany.eeos.program.infra.api.slack.chat.model.Channels;
import com.blackcompany.eeos.program.infra.api.slack.chat.model.ChatPostModel;
import com.blackcompany.eeos.program.infra.api.slack.chat.model.converter.ChatPostModelConverter;
import com.blackcompany.eeos.program.infra.api.slack.chat.support.ChannelGetter;
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
	private final ObjectToJsonMapper<ChatPostModel.Block[]> mapper;

	public CommandProgramResponse notify(ProgramNotificationModel notiModel) {

		ChatPostModel model = modelConverter.from(notiModel);
		SlackChatPostMessageResponse response = post(getTestChannel(), model);

		if(!response.isOk()) throw new SlackChatApiUnAuthException(response.getError());

		return responseConverter.from(notiModel.getId());
	}

	private ChannelGetter getNotiChannel(){
		return () -> channels.getECONOVATION_NOTIFICATION();
	}

	private ChannelGetter getSmallTalkChannel(){
		return () -> channels.getECONOVATION_SMALL_TALK();
	}

	private ChannelGetter getTestChannel(){
		return () -> channels.getBLACK_COMPANY_TEST();
	}


	private SlackChatPostMessageResponse post(ChannelGetter channel, ChatPostModel model){
		return client.post(channel.getChannel(),
				mapper.toJson(model.getMessage()),
				model.getUserName());
	}
}
