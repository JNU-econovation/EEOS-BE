package com.blackcompany.eeos.auth.application.support;

import com.blackcompany.eeos.auth.application.exception.InvalidSignupCodeException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SlackSignupCodeEncoder {

	private static final String ALGORITHM = "AES/GCM/NoPadding";
	private static final int GCM_IV_LENGTH = 12;
	private static final int GCM_TAG_LENGTH = 128;

	private final SecretKeySpec secretKey;

	public SlackSignupCodeEncoder(@Value("${eeos.signup.code-secret}") String secret) {
		byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
		if (keyBytes.length != 16 && keyBytes.length != 24 && keyBytes.length != 32) {
			throw new IllegalArgumentException(
					"eeos.slack.signup.code-secret must be 16, 24, or 32 bytes");
		}
		this.secretKey = new SecretKeySpec(keyBytes, "AES");
	}

	public String encode(String oauthId) {
		try {
			// 초기화 벡터
			byte[] iv = new byte[GCM_IV_LENGTH];
			new SecureRandom().nextBytes(iv);

			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, new GCMParameterSpec(GCM_TAG_LENGTH, iv));

			byte[] encrypted = cipher.doFinal(oauthId.getBytes(StandardCharsets.UTF_8));

			ByteBuffer buffer = ByteBuffer.allocate(iv.length + encrypted.length);
			buffer.put(iv);
			buffer.put(encrypted);

			return Base64.getUrlEncoder().withoutPadding().encodeToString(buffer.array());
		} catch (Exception e) {
			throw new RuntimeException("Slack signup code 암호화 실패", e);
		}
	}

	public String decode(String code) {
		try {
			byte[] decoded = Base64.getUrlDecoder().decode(code);
			ByteBuffer buffer = ByteBuffer.wrap(decoded);

			byte[] iv = new byte[GCM_IV_LENGTH];
			buffer.get(iv);

			byte[] encrypted = new byte[buffer.remaining()];
			buffer.get(encrypted);

			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(GCM_TAG_LENGTH, iv));

			return new String(cipher.doFinal(encrypted), java.nio.charset.StandardCharsets.UTF_8);
		} catch (Exception e) {
			throw new InvalidSignupCodeException();
		}
	}
}
