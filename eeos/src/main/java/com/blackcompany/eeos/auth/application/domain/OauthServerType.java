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
				.filter(oauthServerType -> oauthServerType.getOauthServer().equals(type))
				.findAny()
				.orElseThrow(() -> new NotFoundOauthServerException(type));
	}
}
