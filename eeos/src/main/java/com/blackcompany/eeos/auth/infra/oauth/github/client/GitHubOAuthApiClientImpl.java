package com.blackcompany.eeos.auth.infra.oauth.github.client;

import com.blackcompany.eeos.auth.infra.oauth.github.config.GithubApiFeignConfig;
import com.blackcompany.eeos.auth.infra.oauth.github.dto.GitHubToken;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
		name = "githubOAuthApiClient",
		url = "https://github.com",
		configuration = GithubApiFeignConfig.class)
public interface GitHubOAuthApiClientImpl extends GithubOAuthApiClient {
	@PostMapping(value = "/login/oauth/access_token", produces = "application/json")
	GitHubToken fetchToken(
			@RequestParam("client_id") String client,
			@RequestParam("code") String code,
			@RequestParam("client_secret") String clientSecret,
			@RequestParam("redirect_uri") String redirectUrl);
}
