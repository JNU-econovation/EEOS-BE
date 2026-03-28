package com.blackcompany.eeos.announcement.application.support;

import com.blackcompany.eeos.announcement.infra.gemini.GeminiApiClient;
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
			다음 Slack 공지 메시지를 분석하여 JSON 형식으로 파싱하세요.

			규칙:
			- title: 공지의 제목 (없으면 null)
			- body: 공지의 본문 내용
			- deadline: 마감기한이 명시된 경우 "YYYY-MM-DD" 형식으로 추출, 없으면 null

			반드시 아래 JSON 형식만 반환하세요 (다른 텍스트 없이):
			{"title": "...", "body": "...", "deadline": "YYYY-MM-DD 또는 null"}

			메시지:
			%s
			""";

	private final GeminiApiClient geminiApiClient;
	private final AnnouncementTextParser fallbackParser;
	private final ObjectMapper objectMapper;

	@Value("${gemini.model}")
	private String model;

	@Override
	public ParsedAnnouncement parse(String text) {
		try {
			String responseText = callGemini(text);
			return parseResponse(responseText);
		} catch (Exception e) {
			log.error("Gemini 파싱 실패. rule-based 파서로 fallback합니다. text={}", text, e);
			return fallbackParser.parse(text);
		}
	}

	@Retryable(
			retryFor = Exception.class,
			maxAttempts = 3,
			backoff = @Backoff(delay = 1000, multiplier = 2.0))
	public String callGemini(String text) {
		String prompt = String.format(PROMPT_TEMPLATE, text);
		log.info("Gemini 파싱 요청. model={}, textLength={}", model, text.length());
		String responseText = geminiApiClient.generateContent(model, prompt);
		log.debug("Gemini 응답. responseText={}", responseText);
		return responseText;
	}

	@Recover
	public String recoverCallGemini(Exception e, String text) {
		log.error("Gemini API 3회 재시도 모두 실패. text={}", text, e);
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
			throw new GeminiApiException("Gemini 응답 파싱 실패.", e);
		}
	}
}
