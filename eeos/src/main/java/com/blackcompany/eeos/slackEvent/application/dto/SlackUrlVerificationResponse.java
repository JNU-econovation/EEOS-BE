package com.blackcompany.eeos.slackEvent.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SlackUrlVerificationResponse {
	private String challenge;
}
