package com.blackcompany.eeos.config.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;

class InternalApiKeyFilterTest {

	private static final String VALID_API_KEY = "test-api-key";

	private InternalApiKeyFilter filter;

	@BeforeEach
	void setUp() {
		filter = new InternalApiKeyFilter();
		ReflectionTestUtils.setField(filter, "internalApiKey", VALID_API_KEY);
	}

	@Test
	@DisplayName("유효한 API 키가 있으면 필터 체인을 통과한다")
	void valid_api_key_passes_filter() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("X-EEOS-API-KEY", VALID_API_KEY);
		MockHttpServletResponse response = new MockHttpServletResponse();
		FilterChain chain = mock(FilterChain.class);

		filter.doFilterInternal(request, response, chain);

		verify(chain).doFilter(request, response);
		assertThat(response.getStatus()).isEqualTo(200);
	}

	@Test
	@DisplayName("API 키 헤더가 없으면 401 Unauthorized를 반환한다")
	void missing_api_key_returns_401() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		FilterChain chain = mock(FilterChain.class);

		filter.doFilterInternal(request, response, chain);

		assertThat(response.getStatus()).isEqualTo(401);
		verifyNoInteractions(chain);
	}

	@Test
	@DisplayName("잘못된 API 키이면 401 Unauthorized를 반환한다")
	void invalid_api_key_returns_401() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("X-EEOS-API-KEY", "wrong-key");
		MockHttpServletResponse response = new MockHttpServletResponse();
		FilterChain chain = mock(FilterChain.class);

		filter.doFilterInternal(request, response, chain);

		assertThat(response.getStatus()).isEqualTo(401);
		verifyNoInteractions(chain);
	}
}
