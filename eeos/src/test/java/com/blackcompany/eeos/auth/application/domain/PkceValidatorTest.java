package com.blackcompany.eeos.auth.application.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PkceValidatorTest {

	@Test
	@DisplayName("should return true when code_verifier matches code_challenge")
	void shouldReturnTrueWhenVerifierMatchesChallenge() throws Exception {
		// Given
		String codeVerifier = "dBjftJeZ4CVP-mB92K27uhbUJU1p1r_wW1gFWFOEjXk";
		byte[] digest =
				MessageDigest.getInstance("SHA-256")
						.digest(codeVerifier.getBytes(StandardCharsets.US_ASCII));
		String codeChallenge = Base64.getUrlEncoder().withoutPadding().encodeToString(digest);

		// When & Then
		assertTrue(PkceValidator.validate(codeVerifier, codeChallenge, "S256"));
	}

	@Test
	@DisplayName("should return false when code_verifier does not match")
	void shouldReturnFalseWhenVerifierDoesNotMatch() {
		// When & Then
		assertFalse(PkceValidator.validate("wrong-verifier", "some-challenge", "S256"));
	}

	@Test
	@DisplayName("should throw when unsupported method")
	void shouldThrowWhenUnsupportedMethod() {
		// When & Then
		assertThrows(
				IllegalArgumentException.class,
				() -> PkceValidator.validate("verifier", "challenge", "plain"));
	}
}
