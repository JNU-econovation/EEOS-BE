package com.blackcompany.eeos.member.application.service;

import com.blackcompany.eeos.auth.application.exception.SlackMemberNotFoundException;
import com.blackcompany.eeos.auth.application.repository.OAuthMemberRepository;
import com.blackcompany.eeos.auth.infra.oauth.slack.exception.SlackApiException;
import com.blackcompany.eeos.member.application.model.MemberModel;
import com.blackcompany.eeos.program.infra.api.slack.chat.client.SlackChatApiClient;
import com.blackcompany.eeos.program.infra.api.slack.chat.dto.SlackChatPostMessageResponse;
import com.blackcompany.eeos.program.infra.api.slack.chat.dto.SlackConversationOpenResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SlackDmNotificationService {

	private final SlackChatApiClient slackChatApiClient;
	private final ObjectMapper objectMapper;
	private final OAuthMemberRepository oAuthMemberRepository;

	@Value("${slack.bot.dm.token}")
	private String dmBotToken;

	public void sendSignupLink(MemberModel member, String signupUrl) {
		String oauthId = resolveOauthId(member);
		String message =
				String.format(
						"*EEOS 회원가입 안내*\n\n"
								+ "안녕하세요, %s님!\n\n"
								+ "EEOS가 새롭게 돌아왔습니다!\n\n"
								+ "아래 링크를 통해 EEOS 계정을 등록해 주세요.\n\n"
								+ "가입 링크: %s",
						member.getName(), String.format("%s?slackUserId=%s", signupUrl, oauthId));

		String blocks = buildBlocks(message);
		try {
			SlackConversationOpenResponse openResponse =
					slackChatApiClient.openConversation("Bearer " + dmBotToken, oauthId);

			if (openResponse == null) {
				log.error(
						"Slack conversations.open 응답이 비어있습니다. memberId={}, oauthId={}",
						member.getId(),
						oauthId);
				throw new RuntimeException("Slack conversations.open 응답이 비어있습니다.");
			}

			if (!openResponse.isOk()) {
				log.error(
						"Slack conversations.open 실패 응답. memberId={}, oauthId={}, error={}",
						member.getId(),
						oauthId,
						openResponse.getError());
				throw new SlackApiException("conversations.open", openResponse);
			}

			String dmChannelId = openResponse.getChannelId();
			if (dmChannelId == null || dmChannelId.isBlank()) {
				log.error(
						"Slack DM 채널 ID가 비어있습니다. memberId={}, oauthId={}",
						member.getId(),
						oauthId);
				throw new RuntimeException("Slack DM 채널 ID가 비어있습니다.");
			}

			SlackChatPostMessageResponse response =
					slackChatApiClient.post("Bearer " + dmBotToken, dmChannelId, blocks, "EEOS Bot");

			if (response == null) {
				log.error(
						"Slack DM 응답이 비어있습니다. memberId={}, oauthId={}, dmChannelId={}",
						member.getId(),
						oauthId,
						dmChannelId);
				throw new RuntimeException("Slack DM 응답이 비어있습니다.");
			}

			if (!response.isOk()) {
				log.error(
						"Slack DM 발송 실패 응답. memberId={}, oauthId={}, dmChannelId={}, error={}, channel={}, ts={}",
						member.getId(),
						oauthId,
						dmChannelId,
						response.getError(),
						response.getChannel(),
						response.getTimestamp());
				throw new SlackApiException("chat.postMessage", response);
			}

			log.info(
					"Slack DM 발송 성공. memberId={}, oauthId={}, dmChannelId={}, channel={}, ts={}",
					member.getId(),
					oauthId,
					dmChannelId,
					response.getChannel(),
					response.getTimestamp());
		} catch (FeignException e) {
			log.error(
					"Slack API 호출 예외. memberId={}, oauthId={}, status={}, responseBody={}",
					member.getId(),
					oauthId,
					e.status(),
					e.contentUTF8(),
					e);
			throw e;
		}
	}

	private String resolveOauthId(MemberModel member) {
		return oAuthMemberRepository
				.findByMemberId(member.getId())
				.map(oauthMember -> oauthMember.getOauthId())
				.orElseThrow(SlackMemberNotFoundException::new);
	}

	private String buildBlocks(String text) {
		try {
			return objectMapper.writeValueAsString(
					List.of(Map.of("type", "section", "text", Map.of("type", "mrkdwn", "text", text))));
		} catch (JsonProcessingException e) {
			log.error("Slack 메시지 블록 직렬화 실패", e);
			throw new RuntimeException("Slack 메시지 블록 생성 실패", e);
		}
	}
}
