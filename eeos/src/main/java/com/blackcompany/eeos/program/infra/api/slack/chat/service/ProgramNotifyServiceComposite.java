package com.blackcompany.eeos.program.infra.api.slack.chat.service;

import com.blackcompany.eeos.program.application.dto.CommandProgramResponse;
import com.blackcompany.eeos.program.application.model.ProgramNotificationModel;
import com.blackcompany.eeos.program.infra.api.slack.chat.client.SlackChatApiClient;
import com.blackcompany.eeos.program.infra.api.slack.chat.dto.SlackChatPostMessageResponse;
import com.blackcompany.eeos.program.infra.api.slack.chat.model.ChatPostModel;
import com.blackcompany.eeos.program.infra.api.slack.chat.model.converter.ChatPostModelConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProgramNotifyServiceComposite {

	private final SlackChatApiClient client;
	private final ChatPostModelConverter converter;

	public CommandProgramResponse notify(ProgramNotificationModel notificationModel) {

		ChatPostModel model = converter.from(notificationModel);
		SlackChatPostMessageResponse response = client.post(model);

		/** return 부분 수정 */
		return CommandProgramResponse.builder().programId(notificationModel.getId()).build();
	}
}
