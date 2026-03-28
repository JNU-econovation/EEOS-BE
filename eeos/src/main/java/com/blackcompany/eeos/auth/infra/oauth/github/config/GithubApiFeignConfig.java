package com.blackcompany.eeos.auth.infra.oauth.github.config;

import com.blackcompany.eeos.auth.infra.oauth.github.client.GithubApiErrorDecoder;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class GithubApiFeignConfig {
	@Bean
	public ErrorDecoder githubErrorDecoder(ObjectMapper objectMapper) {
		return new GithubApiErrorDecoder(objectMapper);
	}
}
