package com.blackcompany.eeos.auth.infra.oauth.slack.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@PropertySource("classpath:/env.properties")
@Component
@Setter
@Getter
public class SlackOauthConfig {
	@Value("${oauth.provider.slack.test}")
	private String test;

	@Value("${oauth.provider.slack.clientId}")
	private String clientId;

	@Value("${oauth.provider.slack.clientSecret}")
	private String clientSecret;
}
