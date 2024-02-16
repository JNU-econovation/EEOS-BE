package com.blackcompany.eeos.auth.infra.oauth.slack.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.blackcompany.eeos.auth.application.domain.OauthMemberModel;
import com.blackcompany.eeos.auth.application.domain.OauthServerType;
import com.blackcompany.eeos.auth.fixture.FakeSlackApiClientFixture;
import com.blackcompany.eeos.auth.infra.oauth.slack.config.SlackOauthConfig;
import com.blackcompany.eeos.auth.infra.oauth.slack.converter.OauthModelConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {SlackOauthConfig.class, OauthModelConverter.class})
class SlackOauthMemberClientTest {
	SlackOauthMemberClient slackOauthMemberClient;

	@BeforeEach
	public void beforeEach() {
		SlackOauthConfig slackOauthConfig = new SlackOauthConfig();
		slackOauthConfig.setClientId("client_id");
		slackOauthConfig.setClientSecret("client_secret");

		OauthModelConverter oauthModelConverter = new OauthModelConverter();
		slackOauthMemberClient =
				new SlackOauthMemberClient(slackOauthConfig, new FakeSlackApiClient(), oauthModelConverter);
	}

	@Test
	@DisplayName("슬랙 api에 요청하여 슬랙 정보를 가져온다.")
	void fetch() {
		// when
		OauthMemberModel model = slackOauthMemberClient.fetch("code", "uri");

		// then
		assertEquals(model.getOauthId(), FakeSlackApiClientFixture.토큰_가져오기_성공_응답().getUserId());
		assertEquals(model.getOauthServerType(), OauthServerType.SLACK);
		assertEquals(model.getName(), FakeSlackApiClientFixture.멤버_가져오기_성공_응답().getName());
	}
}
