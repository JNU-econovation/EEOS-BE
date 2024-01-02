package com.blackcompany.eeos.auth.infra.oauth.slack.support;

import com.blackcompany.eeos.auth.infra.oauth.slack.dto.SlackApiResponse;

@FunctionalInterface
public interface SlackQuery<T extends SlackApiResponse, K> {
	T execute(K request);
}
