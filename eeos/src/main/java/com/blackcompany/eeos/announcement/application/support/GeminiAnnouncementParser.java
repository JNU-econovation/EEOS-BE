package com.blackcompany.eeos.announcement.application.support;

import com.blackcompany.eeos.announcement.application.exception.GeminiApiException;
import com.blackcompany.eeos.announcement.application.model.ParsedAnnouncement;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Slf4j
@Primary
@Component
@RequiredArgsConstructor
public class GeminiAnnouncementParser implements AnnouncementParser {

	private static final String PROMPT_TEMPLATE =
			"""
			다음 Slack 공지 메시지를 분석하여 핵심 내용만 요약한 후, 반드시 지정된 JSON 형식으로만 반환하세요.

			규칙:
			1. title: 공지의 제목 (예시: "[ 깃행사 사후 과제 안내 ]"), 제목으로 볼 만한 문구가 없으면 null
			2. body: 공지의 본문 내용을 다음 4가지 항목(내용, 대상, 기한, 링크)을 포함하여 1~2줄씩 핵심만 요약한 문장 (불필요한 인사말이나 미사여구는 모두 제외할 것)
			3. deadline: 마감기한이 명시된 경우 "YYYY-MM-DD" 형식으로 추출, 없으면 null
			4. URL은 Slack이 감싼 < > 기호를 모두 제거하고 순수 URL만 사용할 것 (예: <https://example.com> → https://example.com)
			5. 슬랙 유저 아이디값 (예시 : @U08PZVC89P9)은 제거할것

			반드시 다른 텍스트나 설명 없이 아래 JSON 객체만 반환하세요:
			{
				"title": "...",
				"body": "내용: ...\\n대상: ...\\n기한: ...\\n링크: ...",
				"deadline": "YYYY-MM-DD 또는 null"
			}

			메시지:
			%s
			""";

	private final GeminiApiClient geminiApiClient;
	private final ObjectMapper objectMapper;

	@Value("${gemini.model}")
	private String model;

	@Override
	@Retryable(
			retryFor = GeminiApiException.class,
			maxAttempts = 3,
			backoff = @Backoff(delay = 1000, multiplier = 2.0))
	public ParsedAnnouncement parse(String text) {
		String prompt = String.format(PROMPT_TEMPLATE, text);
		log.info("Gemini 파싱 요청. model={}, textLength={}", model, text.length());
		String responseText = geminiApiClient.generateContent(model, prompt);
		log.debug("Gemini 응답. responseText={}", responseText);
		return parseResponse(responseText);
	}

	@Recover
	public ParsedAnnouncement recoverParse(GeminiApiException e, String text) {
		throw new GeminiApiException("Gemini API 3회 재시도 모두 실패.", e);
	}

	private ParsedAnnouncement parseResponse(String responseText) {
		String cleaned = responseText.replaceAll("```json\\s*", "").replaceAll("```\\s*", "").trim();
		try {
			JsonNode parsed = objectMapper.readTree(cleaned);

			String title = parsed.path("title").isNull() ? null : parsed.path("title").asText(null);
			String body = parsed.path("body").asText("");
			String deadlineStr =
					parsed.path("deadline").isNull() ? null : parsed.path("deadline").asText(null);

			LocalDate deadline = null;
			if (deadlineStr != null && !deadlineStr.isBlank() && !"null".equalsIgnoreCase(deadlineStr)) {
				deadline = LocalDate.parse(deadlineStr);
			}

			return new ParsedAnnouncement(title, body, deadline);
		} catch (Exception e) {
			log.error("Gemini 응답 파싱 실패. responseText={}", responseText, e);
			throw new IllegalStateException("Gemini 응답 파싱 실패.", e);
		}
	}
}
