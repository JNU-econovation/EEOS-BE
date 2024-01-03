package com.blackcompany.eeos.auth.presentation.support;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.blackcompany.eeos.auth.application.exception.NotFoundHeaderTokenException;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HeaderTokenExtractorTest {
	public static final String AUTHORIZATION = "Authorization";
	private static final String BEARER_TOKEN_PREFIX = "Bearer ";

	@Mock HttpServletRequest request;
	HeaderTokenExtractor headerTokenExtractor;

	@BeforeEach
	void setUp() {
		headerTokenExtractor = new HeaderTokenExtractor();
	}

	@Test
	@DisplayName("인증 헤더가 존재하지 않으면 예외가 발생한다.")
	void not_found_cookie_exception() {
		// given
		Mockito.when(request.getHeader(AUTHORIZATION)).thenReturn(null);

		// when & then
		assertThrows(NotFoundHeaderTokenException.class, () -> headerTokenExtractor.extract(request));
	}

	@Test
	@DisplayName("인증 헤더의 값에 prefix가 존재하지 않으면 예외가 발생한다.")
	void not_found_token_cookie_exception() {
		String value = "value";
		// given
		when(request.getHeader(AUTHORIZATION)).thenReturn(value);

		// when & then
		assertThrows(NotFoundHeaderTokenException.class, () -> headerTokenExtractor.extract(request));
	}

	@Test
	@DisplayName("인증 헤더에 값이 존재할 때 prefix를 제외한 헤더의 값을 반환한다.")
	void existing_token_cookie() {
		// given
		String value = "value";
		when(request.getHeader(AUTHORIZATION)).thenReturn(BEARER_TOKEN_PREFIX + value);

		// when
		String extract = headerTokenExtractor.extract(request);

		// then
		assertEquals(value, extract);
	}
}
