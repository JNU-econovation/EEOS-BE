package com.blackcompany.eeos.slackEvent.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EeosSlackMessageForwardRequest {

	private String eventId;
	private String teamId;
	private String channelId;
	private String userId;
	private String text;
	private String messageTs;
	private String threadTs;
}
