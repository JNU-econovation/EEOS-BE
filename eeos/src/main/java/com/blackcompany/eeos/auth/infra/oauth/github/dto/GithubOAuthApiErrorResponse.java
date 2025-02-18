package com.blackcompany.eeos.auth.infra.oauth.github.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GithubOAuthApiErrorResponse implements GithubErrorResponse {
	private String error;

	@JsonProperty("error_description")
	private String errorDescription;

	@JsonProperty("error_uri")
	private String errorUri;

	@Override
	public String getFormattedMessage() {
		return String.format(
				"Github API 호출 실패 - 에러: %s, 상세: %s, 참고 문서: %s", error, errorDescription, errorUri);
	}
}
