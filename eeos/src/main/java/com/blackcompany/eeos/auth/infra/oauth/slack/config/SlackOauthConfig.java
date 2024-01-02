package com.blackcompany.eeos.auth.infra.oauth.slack.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:/env.properties")
@Configuration
@Setter
@Getter
public class SlackOauthConfig {

	@Value("${oauth.provider.slack.clientId}")
	private String clientId;

	@Value("${oauth.provider.slack.clientSecret}")
	private String clientSecret;
}
