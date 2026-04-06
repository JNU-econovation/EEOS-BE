package com.blackcompany.eeos.slackEvent.application.usecase;

import com.blackcompany.eeos.slackEvent.application.dto.SlackEventAckResponse;
import com.blackcompany.eeos.slackEvent.application.dto.SlackEventRequest;

public interface HandleSlackEventUsecase {

	SlackEventAckResponse handle(SlackEventRequest request);
}
