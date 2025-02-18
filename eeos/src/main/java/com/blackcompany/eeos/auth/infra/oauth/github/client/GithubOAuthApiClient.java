package com.blackcompany.eeos.auth.infra.oauth.github.client;

import com.blackcompany.eeos.auth.infra.oauth.github.dto.GitHubToken;

public interface GithubOAuthApiClient {
	GitHubToken fetchToken(String client, String code, String clientSecret, String redirectUrl);
}
