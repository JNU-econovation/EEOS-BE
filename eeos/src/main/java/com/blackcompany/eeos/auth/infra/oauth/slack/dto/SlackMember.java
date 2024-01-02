package com.blackcompany.eeos.auth.infra.oauth.slack.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class SlackMember implements SlackApiResponse {
	private boolean ok;

	@JsonProperty("profile")
	private UserProfile profile;

	private String error;

	@Override
	public String getError() {
		return error;
	}

	@Getter
	@AllArgsConstructor
	@Builder
	@NoArgsConstructor
	public static class UserProfile {
		@JsonProperty("display_name")
		private String displayName;
	}

	public String getName() {
		return getProfile().getDisplayName();
	}
}
