package com.blackcompany.eeos.auth.infra.oauth.slack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class SlackErrorApiResponse implements SlackApiResponse {
	private boolean ok;
	private String error;
}
