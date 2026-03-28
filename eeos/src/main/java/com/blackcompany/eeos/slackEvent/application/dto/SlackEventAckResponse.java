package com.blackcompany.eeos.slackEvent.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlackEventAckResponse {

	private boolean accepted;
	private String eventId;
	private String status;

	public static SlackEventAckResponse of(String eventId, String status) {
		return SlackEventAckResponse.builder().accepted(true).eventId(eventId).status(status).build();
	}
}
