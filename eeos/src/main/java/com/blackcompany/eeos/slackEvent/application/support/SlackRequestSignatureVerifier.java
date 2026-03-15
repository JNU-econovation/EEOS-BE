package com.blackcompany.eeos.slackEvent.application.support;

import com.blackcompany.eeos.slackEvent.application.exception.InvalidSlackSignatureException;
import com.blackcompany.eeos.slackEvent.application.exception.SlackReplayAttackException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HexFormat;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SlackRequestSignatureVerifier {

	// slack 관리자 페이지에서 가져온 서명 키
	private final String signingSecret;
	// 이벤트 발생 시점으로부터 event 수신을 허용할 duration
	private final long allowedTimeSkewSeconds;

	public SlackRequestSignatureVerifier(
			@Value("${slack.event.signing-secret:test-signing-secret}") String signingSecret,
			@Value("${slack.event.allowed-time-skew-seconds:300}") long allowedTimeSkewSeconds) {
		this.signingSecret = signingSecret;
		this.allowedTimeSkewSeconds = allowedTimeSkewSeconds;
	}

	// 검증 시작
	public void verify(String timestamp, String signature, String rawBody) {
		long requestTs = parseTimestamp(timestamp);
		validateTimestamp(requestTs);
		validateSignature(requestTs, signature, rawBody);
	}

	private long parseTimestamp(String timestamp) {
		try {
			return Long.parseLong(timestamp);
		} catch (NumberFormatException e) {
			throw new InvalidSlackSignatureException();
		}
	}

	private void validateTimestamp(long requestTs) {
		long now = Instant.now().getEpochSecond();
		if (Math.abs(now - requestTs) > allowedTimeSkewSeconds) {
			throw new SlackReplayAttackException();
		}
	}

	// Signature 검증
	private void validateSignature(long requestTs, String signature, String rawBody) {
		String baseString = "v0:" + requestTs + ":" + rawBody;
		String expected = "v0=" + hmacSha256(baseString, signingSecret);

		boolean valid =
				java.security.MessageDigest.isEqual(
						expected.getBytes(StandardCharsets.UTF_8), signature.getBytes(StandardCharsets.UTF_8));
		if (!valid) {
			throw new InvalidSlackSignatureException();
		}
	}

	// HMAC 으로 해싱
	private String hmacSha256(String message, String secret) {
		try {
			Mac mac = Mac.getInstance("HmacSHA256");
			mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
			byte[] hash = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
			return HexFormat.of().formatHex(hash);
		} catch (Exception e) {
			throw new InvalidSlackSignatureException();
		}
	}
}
