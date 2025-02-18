package com.blackcompany.eeos.auth.infra.oauth.github.client;

import com.blackcompany.eeos.auth.infra.oauth.github.dto.GithubApiErrorResponse;
import com.blackcompany.eeos.auth.infra.oauth.github.dto.GithubOAuthApiErrorResponse;
import com.blackcompany.eeos.auth.infra.oauth.github.dto.GithubUnexpectedErrorResponse;
import com.blackcompany.eeos.auth.infra.oauth.github.exception.GithubApiException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GithubApiErrorDecoder implements ErrorDecoder {
	private final ObjectMapper objectMapper;

	@Override
	public Exception decode(String methodKey, Response response) {
		try {
			String responseBody =
					new String(response.body().asInputStream().readAllBytes(), StandardCharsets.UTF_8);
			JsonNode jsonNode = objectMapper.readTree(responseBody);

			if (isOAuthErrorResponse(jsonNode)) {
				GithubOAuthApiErrorResponse oAuthError =
						objectMapper.readValue(responseBody, GithubOAuthApiErrorResponse.class);
				return new GithubApiException(oAuthError);
			}

			if (isApiErrorResponse(jsonNode)) {
				GithubApiErrorResponse apiError =
						objectMapper.readValue(responseBody, GithubApiErrorResponse.class);
				return new GithubApiException(apiError);
			}

			return new GithubApiException(
					new GithubUnexpectedErrorResponse(
							"Unexpected Github API error response format: " + responseBody));

		} catch (IOException e) {
			return new GithubApiException(
					new GithubUnexpectedErrorResponse("Failed to parse Github API error response", e));
		}
	}

	private boolean isOAuthErrorResponse(JsonNode jsonNode) {
		return jsonNode.has("error") && jsonNode.has("error_description") && jsonNode.has("error_uri");
	}

	private boolean isApiErrorResponse(JsonNode jsonNode) {
		return jsonNode.has("message") && jsonNode.has("documentation_url");
	}
}
