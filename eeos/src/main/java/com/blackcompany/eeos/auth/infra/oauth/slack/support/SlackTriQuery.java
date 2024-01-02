package com.blackcompany.eeos.auth.infra.oauth.slack.support;

import com.blackcompany.eeos.auth.infra.oauth.slack.dto.SlackApiResponse;

@FunctionalInterface
public interface SlackTriQuery<T extends SlackApiResponse, K, U, V> {
	T execute(K k, U u, V v);
}
