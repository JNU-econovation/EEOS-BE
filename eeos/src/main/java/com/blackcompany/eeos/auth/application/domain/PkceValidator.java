package com.blackcompany.eeos.auth.application.domain;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PkceValidator {

	private PkceValidator() {}

	public static boolean validate(String codeVerifier, String codeChallenge, String method) {
		if (!"S256".equals(method)) {
			throw new IllegalArgumentException("Unsupported code_challenge_method: " + method);
		}

		try {
			byte[] digest =
					MessageDigest.getInstance("SHA-256")
							.digest(codeVerifier.getBytes(StandardCharsets.US_ASCII));
			String computed = Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
			return MessageDigest.isEqual(
					computed.getBytes(StandardCharsets.UTF_8),
					codeChallenge.getBytes(StandardCharsets.UTF_8));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("SHA-256 not available", e);
		}
	}
}
