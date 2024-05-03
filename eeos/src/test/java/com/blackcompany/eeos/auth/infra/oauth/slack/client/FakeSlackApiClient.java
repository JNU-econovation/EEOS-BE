package com.blackcompany.eeos.auth.infra.oauth.slack.client;

import com.blackcompany.eeos.auth.fixture.FakeSlackApiClientFixture;
import com.blackcompany.eeos.auth.infra.oauth.slack.dto.SlackMember;
import com.blackcompany.eeos.auth.infra.oauth.slack.dto.SlackToken;

class FakeSlackApiClient implements SlackOauthApiClient {

	@Override
	public SlackToken fetchToken(
			String client, String code, String clientSecret, String redirectUrl) {
		return FakeSlackApiClientFixture.토큰_가져오기_성공_응답();
	}

	@Override
	public SlackMember fetchMember(String token) {
		return FakeSlackApiClientFixture.멤버_가져오기_성공_응답();
	}
}
