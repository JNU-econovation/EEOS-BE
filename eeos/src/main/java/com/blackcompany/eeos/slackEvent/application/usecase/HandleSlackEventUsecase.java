package com.blackcompany.eeos.slackEvent.application.usecase;

import com.blackcompany.eeos.slackEvent.application.dto.SlackEventAckResponse;
import com.blackcompany.eeos.slackEvent.application.dto.SlackEventEnvelopeRequest;

public interface HandleSlackEventUsecase {

	SlackEventAckResponse handle(SlackEventEnvelopeRequest request);
}
