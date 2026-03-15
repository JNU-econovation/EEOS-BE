package com.blackcompany.eeos.slackEvent.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SlackEventEnvelopeRequest {

	private String type;
	private String challenge;
	private String token;

	@JsonProperty("event_id")
	private String eventId;

	@JsonProperty("event_time")
	private Long eventTime;

	@JsonProperty("team_id")
	private String teamId;

	private SlackInnerEvent event;

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class SlackInnerEvent {

		private String type;
		private String channel;
		private String user;
		private String text;
		private String ts;

		@JsonProperty("thread_ts")
		private String threadTs;

		private String subtype;
	}
}
