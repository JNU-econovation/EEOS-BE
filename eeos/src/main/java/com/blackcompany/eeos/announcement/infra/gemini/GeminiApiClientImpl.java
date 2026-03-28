package com.blackcompany.eeos.announcement.infra.gemini;

import com.blackcompany.eeos.announcement.application.exception.GeminiApiException;
import com.blackcompany.eeos.announcement.application.support.GeminiApiClient;
import com.google.genai.Client;
import com.google.genai.errors.ClientException;
import com.google.genai.errors.GenAiIOException;
import com.google.genai.errors.ServerException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GeminiApiClientImpl implements GeminiApiClient {

	@Value("${gemini.api-key}")
	private String apiKey;

	private Client geminiClient;

	@PostConstruct
	void init() {
		if (apiKey == null || apiKey.isBlank()) {
			throw new IllegalStateException("Gemini API 키가 설정되지 않았습니다.");
		}
		geminiClient = Client.builder().apiKey(apiKey).build();
	}

	@Override
	public String generateContent(String model, String prompt) {
		try {
			return geminiClient.models.generateContent(model, prompt, null).text();
		} catch (ClientException e) {
			throw new GeminiApiException(
					String.format("Gemini API 클라이언트 오류. code=%d, status=%s", e.code(), e.status()), e);
		} catch (ServerException e) {
			throw new GeminiApiException(
					String.format("Gemini API 서버 오류. code=%d, status=%s", e.code(), e.status()), e);
		} catch (GenAiIOException e) {
			throw new GeminiApiException("Gemini API 네트워크 오류.", e);
		}
	}
}
