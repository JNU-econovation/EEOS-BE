package com.blackcompany.eeos.auth.application.domain;

import com.blackcompany.eeos.auth.application.exception.NotFoundOauthServerException;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum OauthServerType {
	SLACK("slack");

	private final String oauthServer;

	OauthServerType(String oauthServer) {
		this.oauthServer = oauthServer;
	}

	public static OauthServerType find(String type) {
		return Arrays.stream(values())
				.filter(oauthServerType -> oauthServerType.oauthServer.equals(type))
				.findAny()
				.orElseThrow(() -> new NotFoundOauthServerException(type));
	}

	public static OauthServerType find(OauthServerType type) {
		return Arrays.stream(values())
				.filter(oauthServerType -> oauthServerType.equals(type))
				.findAny()
				.orElseThrow(() -> new NotFoundOauthServerException(type.getOauthServer()));
	}
}
