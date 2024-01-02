package com.blackcompany.eeos.auth.infra.oauth.slack.dto;

public interface SlackApiResponse {
	boolean isOk();

	String getError();
}
