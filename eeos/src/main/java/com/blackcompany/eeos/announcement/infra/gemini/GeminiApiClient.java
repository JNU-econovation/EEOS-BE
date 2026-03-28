package com.blackcompany.eeos.announcement.infra.gemini;

public interface GeminiApiClient {

	String generateContent(String model, String prompt);
}
