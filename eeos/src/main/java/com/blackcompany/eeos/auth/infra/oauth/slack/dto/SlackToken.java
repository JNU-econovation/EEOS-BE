package com.blackcompany.eeos.auth.infra.oauth.slack.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SlackToken implements SlackApiResponse {
	@JsonProperty("ok")
	private boolean ok;

	@JsonProperty("authed_user")
	private AuthedUser authedUser;

	private String error;

	@Override
	public String getError() {
		return error;
	}

	@Getter
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class AuthedUser {
		@JsonProperty("id")
		private String userId;

		@JsonProperty("access_token")
		private String accessToken;
	}

	public String getUserId() {
		return getAuthedUser().getUserId();
	}

	public String getToken() {
		return getAuthedUser().getAccessToken();
	}
}
