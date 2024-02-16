package com.blackcompany.eeos.auth.presentation.support;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.blackcompany.eeos.auth.application.exception.NotFoundCookieException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CookieTokenExtractorTest {
	@Mock HttpServletRequest request;
	CookieTokenExtractor cookieTokenExtractor;

	@BeforeEach
	void setUp() {
		cookieTokenExtractor = new CookieTokenExtractor();
	}

	@Test
	@DisplayName("요청에 쿠키가 없으면 예외가 발생한다.")
	void not_found_cookie_exception() {
		// given
		when(request.getCookies()).thenReturn(null);

		// when & then
		assertThrows(NotFoundCookieException.class, () -> cookieTokenExtractor.extract(request));
	}

	@Test
	@DisplayName("토큰을 키로 가지고 있는 쿠키가 없을 때 예외가 발생한다.")
	void not_found_token_cookie_exception() {
		// given
		Cookie[] cookies = new Cookie[1];
		Cookie cookie = new Cookie("key", "value");
		cookies[0] = cookie;

		when(request.getCookies()).thenReturn(cookies);

		// when & then
		assertThrows(NotFoundCookieException.class, () -> cookieTokenExtractor.extract(request));
	}

	@Test
	@DisplayName("토큰을 키로 가지고 있는 쿠키가 있을 때 쿠키의 값을 반환한다.")
	void existing_token_cookie() {
		// given
		String key = "eeos_token";
		String value = "value";

		Cookie[] cookies = new Cookie[1];
		Cookie cookie = new Cookie(key, value);
		cookies[0] = cookie;

		when(request.getCookies()).thenReturn(cookies);
		System.out.println(request.getCookies().length);

		String extract = cookieTokenExtractor.extract(request);

		// then
		assertEquals(value, extract);
	}
}
