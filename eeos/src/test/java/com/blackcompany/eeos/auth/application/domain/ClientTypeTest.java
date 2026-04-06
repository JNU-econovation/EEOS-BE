package com.blackcompany.eeos.auth.application.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ClientTypeTest {

	@Test
	@DisplayName("should return true when WEB is confidential")
	void shouldReturnTrueWhenWebIsConfidential() {
		// Given
		ClientType clientType = ClientType.WEB;

		// When
		boolean result = clientType.isConfidential();

		// Then
		assertTrue(result);
	}

	@Test
	@DisplayName("should return false when APP is confidential")
	void shouldReturnFalseWhenAppIsConfidential() {
		// Given
		ClientType clientType = ClientType.APP;

		// When
		boolean result = clientType.isConfidential();

		// Then
		assertFalse(result);
	}
}
