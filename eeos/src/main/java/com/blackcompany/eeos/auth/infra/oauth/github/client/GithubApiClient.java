package com.blackcompany.eeos.auth.infra.oauth.github.client;

import com.blackcompany.eeos.auth.infra.oauth.github.dto.GitHubMember;

public interface GithubApiClient {
	GitHubMember getMemberInfo(String bearerToken);
}
