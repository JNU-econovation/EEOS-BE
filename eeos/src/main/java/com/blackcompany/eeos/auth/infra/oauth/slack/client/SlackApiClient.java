package com.blackcompany.eeos.auth.infra.oauth.slack.client;

import com.blackcompany.eeos.auth.infra.oauth.slack.dto.SlackMember;
import com.blackcompany.eeos.auth.infra.oauth.slack.dto.SlackToken;

public interface SlackApiClient {
	SlackToken fetchToken(String client, String code, String clientSecret);

	SlackMember fetchMember(String token);
}
