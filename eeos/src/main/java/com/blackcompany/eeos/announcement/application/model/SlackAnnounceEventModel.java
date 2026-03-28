package com.blackcompany.eeos.announcement.application.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SlackAnnounceEventModel {

	private Long id;
	private String eventId;
	private String teamId;
	private String channelId;
	private String userId;
	private String messageTs;

	public static SlackAnnounceEventModel create(
			String eventId, String teamId, String channelId, String userId, String messageTs) {
		return SlackAnnounceEventModel.builder()
				.eventId(eventId)
				.teamId(teamId)
				.channelId(channelId)
				.userId(userId)
				.messageTs(messageTs)
				.build();
	}
}
