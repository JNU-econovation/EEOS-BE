package com.blackcompany.eeos.auth.fixture;

import com.blackcompany.eeos.auth.infra.oauth.slack.dto.SlackMember;
import com.blackcompany.eeos.auth.infra.oauth.slack.dto.SlackMember.UserProfile;
import com.blackcompany.eeos.auth.infra.oauth.slack.dto.SlackToken;
import com.blackcompany.eeos.auth.infra.oauth.slack.dto.SlackToken.AuthedUser;

public class FakeSlackApiClientFixture {
	private static final String userId = "userId";
	private static final String token = "token";
	private static final String name = "oauth_name";

	public static SlackToken successSlackToken() {
		return SlackToken.builder()
				.ok(true)
				.authedUser(AuthedUser.builder().userId(userId).accessToken(token).build())
				.error("")
				.build();
	}

	public SlackToken failSlackToken(String client, String code, String clientSecret) {
		return SlackToken.builder()
				.ok(false)
				.authedUser(AuthedUser.builder().userId("userId").accessToken("access_token").build())
				.error("error_message")
				.build();
	}

	public SlackMember failSlackMember(String token) {
		return SlackMember.builder()
				.ok(false)
				.profile(UserProfile.builder().displayName("oauth_name").build())
				.error("error_message")
				.build();
	}

	public static SlackMember successSlackMember() {
		return SlackMember.builder()
				.ok(true)
				.profile(UserProfile.builder().displayName(name).build())
				.error("")
				.build();
	}
}
