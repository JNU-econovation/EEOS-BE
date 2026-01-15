package com.blackcompany.eeos.notification.application.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.blackcompany.eeos.program.infra.api.slack.chat.client.SlackChatApiClient;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SlackNotificationService {

	private final SlackChatApiClient slackChatApiClient;
	private final ObjectMapper objectMapper;

	@Value("${slack.bot.black-company.eeos}")
	private String botToken;

	@Value("${slack.channel.black-company.error-report}")
	private String errorReportChannel;

	public void sendSchedulerFailureMessage(String schedulerName, String errorMessage){
		String message = String.format(
			":rotating_light: *스케줄러 실행 실패 알림*\n\n" +
				"*Scheduler*\n `%s`\n\n" +
				"*Failed At*\n`%s`\n\n" +
				"*Error Message*\n`%s`\n\n",
			schedulerName,
			LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
			errorMessage
		);
		sendMessage(message);
	}

	private void sendMessage(String text){
		try{
			String blocks = objectMapper.writeValueAsString(List.of(Map.of("type", "section", "text", Map.of("type","mrkdwn", "text", text))));

			slackChatApiClient.post(
				"Bearer " + botToken, errorReportChannel, blocks, "EEOS Scheduler Bot");

		} catch (Exception e) {
			log.error("Slack 알림 전송 실패", e);

		}
	}
}
