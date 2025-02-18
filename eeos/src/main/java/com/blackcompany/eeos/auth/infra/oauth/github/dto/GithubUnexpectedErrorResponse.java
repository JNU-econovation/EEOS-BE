package com.blackcompany.eeos.auth.infra.oauth.github.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GithubUnexpectedErrorResponse implements GithubErrorResponse {
	private String message;
	private Exception cause;

	public GithubUnexpectedErrorResponse(String message) {
		this(message, null);
	}

	@Override
	public String getFormattedMessage() {
		if (cause != null) {
			return String.format("Github API 처리 실패 - %s (원인: %s)", message, cause.getMessage());
		}
		return String.format("Github API 처리 실패 - %s", message);
	}
}
