package com.blackcompany.eeos.auth.fixture;

import com.blackcompany.eeos.auth.infra.oauth.slack.dto.SlackMember;
import com.blackcompany.eeos.auth.infra.oauth.slack.dto.SlackMember.UserProfile;
import com.blackcompany.eeos.auth.infra.oauth.slack.dto.SlackToken;
import com.blackcompany.eeos.auth.infra.oauth.slack.dto.SlackToken.AuthedUser;

public class FakeSlackApiClientFixture {
	private static final String userId = "userId";
	private static final String token = "token";
	private static final String name = "oauth_name";

	public static SlackToken 토큰_가져오기_성공_응답() {
		return SlackToken.builder()
				.ok(true)
				.authedUser(AuthedUser.builder().userId(userId).accessToken(token).build())
				.error("")
				.build();
	}

	public static SlackMember 멤버_가져오기_성공_응답() {
		return SlackMember.builder()
				.ok(true)
				.profile(UserProfile.builder().displayName(name).build())
				.error("")
				.build();
	}
}
