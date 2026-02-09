package com.blackcompany.eeos.notification.application.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationSendResults {
	private List<String> successTokens;
	private List<String> retryTokens;
	private List<String> invalidTokens;
	private List<String> permanentlyFailedTokens;
}
