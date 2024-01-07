package com.blackcompany.eeos.auth.infra.oauth.slack.support;

import com.blackcompany.eeos.auth.infra.oauth.slack.dto.SlackApiResponse;

@FunctionalInterface
public interface SlackFourQuery<T extends SlackApiResponse, K, U, V, S> {
	T execute(K k, U u, V v, S s);
}
