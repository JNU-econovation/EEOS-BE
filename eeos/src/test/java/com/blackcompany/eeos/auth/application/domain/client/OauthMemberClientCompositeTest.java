package com.blackcompany.eeos.auth.application.domain.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.blackcompany.eeos.auth.application.domain.OauthMemberModel;
import com.blackcompany.eeos.auth.application.domain.OauthServerType;
import com.blackcompany.eeos.auth.fixture.FakeOauthMemberClient;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OauthMemberClientCompositeTest {
	@Test
	@DisplayName("전달받은 인증 서버에게 인증된 유저 정보를 요청하여 유저 정보를 받는다.")
	void fetch() {
		// given
		String oauthServerType = OauthServerType.SLACK.getOauthServer();
		String authCode = "code";
		String uri = "uri";
		Set<OauthMemberClient> set = new HashSet<>();
		FakeOauthMemberClient fakeOauthMemberClient = new FakeOauthMemberClient();
		set.add(fakeOauthMemberClient);

		OauthMemberClientComposite oauthMemberClientComposite = new OauthMemberClientComposite(set);

		// when
		OauthMemberModel oauthMemberModel =
				oauthMemberClientComposite.fetch(oauthServerType, authCode, uri);

		// then
		assertEquals(
				oauthMemberModel.getOauthId(), fakeOauthMemberClient.fetch(authCode, uri).getOauthId());
		assertEquals(oauthMemberModel.getName(), fakeOauthMemberClient.fetch(authCode, uri).getName());
		assertEquals(
				oauthMemberModel.getOauthServerType(),
				fakeOauthMemberClient.fetch(authCode, uri).getOauthServerType());
	}
}
