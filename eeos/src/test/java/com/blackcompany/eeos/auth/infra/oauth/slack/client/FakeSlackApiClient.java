package com.blackcompany.eeos.auth.infra.oauth.slack.client;

import com.blackcompany.eeos.auth.fixture.FakeSlackApiClientFixture;
import com.blackcompany.eeos.auth.infra.oauth.slack.dto.SlackMember;
import com.blackcompany.eeos.auth.infra.oauth.slack.dto.SlackToken;

class FakeSlackApiClient implements SlackApiClient {

	@Override
	public SlackToken fetchToken(String client, String code, String clientSecret) {
		return FakeSlackApiClientFixture.successSlackToken();
	}

	@Override
	public SlackMember fetchMember(String token) {
		return FakeSlackApiClientFixture.successSlackMember();
	}
}
