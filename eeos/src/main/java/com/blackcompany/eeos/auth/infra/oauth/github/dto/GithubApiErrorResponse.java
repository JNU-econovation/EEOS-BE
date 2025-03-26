package com.blackcompany.eeos.auth.infra.oauth.github.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GithubApiErrorResponse implements GithubErrorResponse {
	private String message;

	@JsonProperty("documentation_url")
	private String documentationUrl;

	private String status;

	@Override
	public String getFormattedMessage() {
		return String.format("Github API 호출 실패 - 에러: %s, 참고 문서: %s", message, documentationUrl);
	}
}
