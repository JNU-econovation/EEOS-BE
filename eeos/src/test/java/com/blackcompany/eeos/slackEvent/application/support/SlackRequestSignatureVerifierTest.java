package com.blackcompany.eeos.slackEvent.application.support;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.blackcompany.eeos.slackEvent.application.exception.InvalidSlackSignatureException;
import com.blackcompany.eeos.slackEvent.application.exception.SlackReplayAttackException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HexFormat;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SlackRequestSignatureVerifierTest {

	private static final String SIGNING_SECRET = "test-signing-secret";
	private static final long ALLOWED_TIME_SKEW_SECONDS = 300L;

	private SlackRequestSignatureVerifier verifier;

	@BeforeEach
	void setUp() {
		verifier = new SlackRequestSignatureVerifier(SIGNING_SECRET, ALLOWED_TIME_SKEW_SECONDS);
	}

	@Test
	@DisplayName("유효한 타임스탬프와 서명이면 검증을 통과한다")
	void verify_success() {
		// given
		String rawBody = "{\"type\":\"url_verification\",\"challenge\":\"challenge-value\"}";
		String timestamp = String.valueOf(Instant.now().getEpochSecond());
		String signature = createSlackSignature(timestamp, rawBody);

		// when & then
		assertThatCode(() -> verifier.verify(timestamp, signature, rawBody)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("서명이 일치하지 않으면 InvalidSlackSignatureException을 던진다")
	void verify_fail_when_signature_not_match() {
		// given
		String rawBody = "{\"type\":\"event_callback\",\"event_id\":\"Ev123\"}";
		String timestamp = String.valueOf(Instant.now().getEpochSecond());
		String invalidSignature = "v0=deadbeef";

		// when & then
		assertThatThrownBy(() -> verifier.verify(timestamp, invalidSignature, rawBody))
				.isInstanceOf(InvalidSlackSignatureException.class);
	}

	@Test
	@DisplayName("허용 시간을 초과한 타임스탬프면 SlackReplayAttackException을 던진다")
	void verify_fail_when_timestamp_expired() {
		// given
		String rawBody = "{\"type\":\"event_callback\",\"event_id\":\"Ev123\"}";
		String expiredTimestamp = String.valueOf(Instant.now().minusSeconds(301L).getEpochSecond());
		String signature = createSlackSignature(expiredTimestamp, rawBody);

		// when & then
		assertThatThrownBy(() -> verifier.verify(expiredTimestamp, signature, rawBody))
				.isInstanceOf(SlackReplayAttackException.class);
	}

	private String createSlackSignature(String timestamp, String rawBody) {
		try {
			Mac mac = Mac.getInstance("HmacSHA256");
			SecretKeySpec keySpec =
					new SecretKeySpec(SIGNING_SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
			mac.init(keySpec);
			byte[] hash =
					mac.doFinal(("v0:" + timestamp + ":" + rawBody).getBytes(StandardCharsets.UTF_8));
			return "v0=" + HexFormat.of().formatHex(hash);
		} catch (Exception e) {
			throw new IllegalStateException("테스트용 서명 생성 실패", e);
		}
	}
}
