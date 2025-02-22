package com.blackcompany.eeos.auth.infra.oauth.github.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "oauth.provider.github")
@Getter
@Setter
public class GitHubOAuthConfig {
	private String clientId;
	private String clientSecret;
}
