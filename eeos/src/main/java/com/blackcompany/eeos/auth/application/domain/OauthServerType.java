package com.blackcompany.eeos.auth.application.domain;

import com.blackcompany.eeos.auth.application.exception.NotFoundOauthServerException;
import java.util.Arrays;

public enum OauthServerType {
	SLACK("slack");

	private final String oauthServer;

	OauthServerType(String oauthServer) {
		this.oauthServer = oauthServer;
	}

	public static OauthServerType find(String oauthServer) {
		return Arrays.stream(values())
				.filter(oauthServerType -> oauthServerType.oauthServer.equals(oauthServer))
				.findAny()
				.orElseThrow(() -> new NotFoundOauthServerException(oauthServer));
	}
}
