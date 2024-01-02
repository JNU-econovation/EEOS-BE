package com.blackcompany.eeos.auth.application.domain.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.blackcompany.eeos.auth.application.domain.OauthMemberModel;
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
		String oauthServerType = "fake";
		String authCode = "code";
		Set<OauthMemberClient> set = new HashSet<>();
		FakeOauthMemberClient fakeOauthMemberClient = new FakeOauthMemberClient();
		set.add(fakeOauthMemberClient);

		OauthMemberClientComposite oauthMemberClientComposite = new OauthMemberClientComposite(set);

		// when
		OauthMemberModel oauthMemberModel = oauthMemberClientComposite.fetch(oauthServerType, authCode);

		// then
		assertEquals(oauthMemberModel.getOauthId(), fakeOauthMemberClient.fetch(authCode).getOauthId());
		assertEquals(oauthMemberModel.getName(), fakeOauthMemberClient.fetch(authCode).getName());
		assertEquals(
				oauthMemberModel.getOauthServerType(),
				fakeOauthMemberClient.fetch(authCode).getOauthServerType());
	}
}
