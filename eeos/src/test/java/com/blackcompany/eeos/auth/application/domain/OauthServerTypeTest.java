package com.blackcompany.eeos.auth.application.domain;

import static org.junit.jupiter.api.Assertions.*;

import com.blackcompany.eeos.auth.application.exception.NotFoundOauthServerException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OauthServerTypeTest {

	@Test
	@DisplayName("존재하는 서버이면 해당 서버타입을 반환한다.")
	void success_find_server() {
		// given
		String type = "slack";

		// when
		OauthServerType oauthServerType = OauthServerType.find(type);

		// then
		Assertions.assertEquals(OauthServerType.SLACK, oauthServerType);
	}

	@Test
	@DisplayName("존재하지 않는 서버이면 에러가 발생한다.")
	void fail_find_server() {
		// given
		String type = "nothing";

		// when
		assertThrows(NotFoundOauthServerException.class, () -> OauthServerType.find(type));
	}
}
