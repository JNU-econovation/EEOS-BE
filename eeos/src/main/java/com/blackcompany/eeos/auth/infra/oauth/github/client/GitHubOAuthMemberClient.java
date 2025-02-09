package com.blackcompany.eeos.auth.infra.oauth.github.client;

import com.blackcompany.eeos.auth.application.domain.OauthMemberModel;
import com.blackcompany.eeos.auth.application.domain.OauthServerType;
import com.blackcompany.eeos.auth.application.domain.client.OauthMemberClient;
import com.blackcompany.eeos.auth.infra.oauth.github.config.GitHubOAuthConfig;
import com.blackcompany.eeos.auth.infra.oauth.github.dto.GitHubMember;
import com.blackcompany.eeos.auth.infra.oauth.github.dto.GitHubToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GitHubOAuthMemberClient implements OauthMemberClient {
	private final GitHubOAuthConfig oauthConfig;
	private final GitHubOAuthApiClientImpl githubOAuthApi;
	private final GithubApiClientImpl githubApi;

	@Override
	public OauthServerType support() {
		return OauthServerType.GITHUB;
	}

	@Override
	public OauthMemberModel fetch(String code, String uri) {
		GitHubToken token =
				githubOAuthApi.fetchToken(
						oauthConfig.getClientId(), code, oauthConfig.getClientSecret(), uri);
		GitHubMember githubMember = githubApi.getMemberInfo(formatBearerToken(token.getAccessToken()));

		return OauthMemberModel.builder()
				.oauthId(githubMember.getId().toString())
				.oauthServerType(OauthServerType.GITHUB)
				.isRequiresAdditionalInfo(true)
				.build();
	}
}
