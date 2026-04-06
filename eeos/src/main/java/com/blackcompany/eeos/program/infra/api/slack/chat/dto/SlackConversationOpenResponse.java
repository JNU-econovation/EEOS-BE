package com.blackcompany.eeos.program.infra.api.slack.chat.dto;

import com.blackcompany.eeos.auth.infra.oauth.slack.dto.SlackApiResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SlackConversationOpenResponse implements SlackApiResponse {

	private boolean ok;
	private Channel channel;
	private String error;

	@Override
	public String getError() {
		return error;
	}

	public String getChannelId() {
		return channel != null ? channel.getId() : null;
	}

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class Channel {
		private String id;

		@JsonProperty("is_im")
		private boolean isIm;

		private String user;
	}
}
