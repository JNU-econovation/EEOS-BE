package com.blackcompany.eeos.auth.infra.oauth.github.client;

import com.blackcompany.eeos.auth.infra.oauth.github.config.GithubApiFeignConfig;
import com.blackcompany.eeos.auth.infra.oauth.github.dto.GitHubMember;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
		name = "githubApiClient",
		url = "https://api.github.com",
		configuration = GithubApiFeignConfig.class)
public interface GithubApiClientImpl extends GithubApiClient {

	@GetMapping(value = "/user")
	GitHubMember getMemberInfo(@RequestHeader("Authorization") String bearerToken);
}
