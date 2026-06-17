package com.blackcompany.eeos.auth.presentation.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.blackcompany.eeos.auth.application.domain.ClientType;
import com.blackcompany.eeos.auth.application.domain.TokenModel;
import com.blackcompany.eeos.auth.application.exception.InvalidClientException;
import com.blackcompany.eeos.auth.application.service.ClientService;
import com.blackcompany.eeos.auth.application.service.OAuth2LoginService;
import com.blackcompany.eeos.auth.application.service.TokenExchangeService;
import com.blackcompany.eeos.auth.persistence.client.ClientEntity;
import com.blackcompany.eeos.auth.presentation.support.AuthCookieManager;
import java.lang.reflect.Field;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

@ExtendWith(MockitoExtension.class)
class OAuth2ControllerTest {

	@Mock private ClientService clientService;
	@Mock private OAuth2LoginService oAuth2LoginService;
	@Mock private TokenExchangeService tokenExchangeService;
	@Mock private AuthCookieManager cookieManager;

	@InjectMocks private OAuth2Controller controller;

	@BeforeEach
	void setUp() throws Exception {
		Field loginPageUrlField = OAuth2Controller.class.getDeclaredField("loginPageUrl");
		loginPageUrlField.setAccessible(true);
		loginPageUrlField.set(controller, "http://localhost:3000/login");
	}

	@Nested
	@DisplayName("/authorize 엔드포인트")
	class Authorize {

		@Test
		@DisplayName("response_type이 code가 아니면 400 반환")
		void bad_request_when_invalid_response_type() {
			ResponseEntity<Void> result =
					controller.authorize("client1", "http://app/callback", "token", "state1", null, null);

			assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
		}

		@Test
		@DisplayName("APP 클라이언트가 code_challenge 없이 요청하면 400 반환")
		void bad_request_when_app_without_code_challenge() {
			ClientEntity appClient =
					ClientEntity.builder().clientId("app1").clientType(ClientType.APP).build();
			when(clientService.findAndValidateRedirectUri("app1", "http://app/callback"))
					.thenReturn(appClient);

			ResponseEntity<Void> result =
					controller.authorize("app1", "http://app/callback", "code", "state1", null, null);

			assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
		}

		@Test
		@DisplayName("유효한 요청이면 302 로그인 페이지 리다이렉트")
		void redirect_to_login_page() {
			ClientEntity webClient =
					ClientEntity.builder().clientId("web1").clientType(ClientType.WEB).build();
			when(clientService.findAndValidateRedirectUri("web1", "http://web/callback"))
					.thenReturn(webClient);

			ResponseEntity<Void> result =
					controller.authorize("web1", "http://web/callback", "code", "state1", null, null);

			assertEquals(HttpStatus.FOUND, result.getStatusCode());
			assertTrue(result.getHeaders().getLocation().toString().contains("client_id=web1"));
		}
	}

	@Nested
	@DisplayName("/login/oauth2 엔드포인트")
	class LoginOAuth2 {

		@Test
		@DisplayName("유효하지 않은 client_id면 400 반환")
		void bad_request_when_invalid_client() {
			when(clientService.findAndValidateRedirectUri("bad", "http://x"))
					.thenThrow(new InvalidClientException());

			ResponseEntity<Map<String, String>> result =
					controller.login(
							"bad",
							"http://x",
							"state",
							"e",
							"p",
							null,
							null,
							new MockHttpServletRequest(),
							new MockHttpServletResponse());

			assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
		}

		@Test
		@DisplayName("WEB 클라이언트 로그인 성공 시 쿠키 설정 + 200 + redirectUrl 반환")
		void web_login_sets_cookies_and_returns_redirect_url() {
			ClientEntity webClient =
					ClientEntity.builder().clientId("web1").clientType(ClientType.WEB).build();
			when(clientService.findAndValidateRedirectUri("web1", "http://web/callback"))
					.thenReturn(webClient);

			TokenModel tokenModel =
					TokenModel.builder()
							.accessToken("at")
							.refreshToken("rt")
							.accessExpiredTime(99999L)
							.build();
			when(oAuth2LoginService.loginForWeb(
							"web1", "http://web/callback", "user@test.com", "pw", "127.0.0.1"))
					.thenReturn(tokenModel);
			when(cookieManager.setAccessTokenCookie("at"))
					.thenReturn(ResponseCookie.from("eeos_access_token", "at").build());
			when(cookieManager.setRefreshTokenCookie("rt"))
					.thenReturn(ResponseCookie.from("eeos_refresh_token", "rt").build());

			MockHttpServletRequest request = new MockHttpServletRequest();
			request.setRemoteAddr("127.0.0.1");
			MockHttpServletResponse response = new MockHttpServletResponse();

			ResponseEntity<Map<String, String>> result =
					controller.login(
							"web1",
							"http://web/callback",
							"state1",
							"user@test.com",
							"pw",
							null,
							null,
							request,
							response);

			assertEquals(HttpStatus.OK, result.getStatusCode());
			assertTrue(result.getBody().get("redirectUrl").contains("http://web/callback"));
			assertTrue(result.getBody().get("redirectUrl").contains("state=state1"));
			assertTrue(response.getHeader(HttpHeaders.SET_COOKIE).contains("eeos_access_token"));
		}

		@Test
		@DisplayName("APP 클라이언트 로그인 성공 시 200 + redirectUrl(authorization_code 포함) 반환")
		void app_login_returns_redirect_url_with_code() {
			ClientEntity appClient =
					ClientEntity.builder().clientId("app1").clientType(ClientType.APP).build();
			when(clientService.findAndValidateRedirectUri("app1", "http://app/callback"))
					.thenReturn(appClient);
			when(oAuth2LoginService.loginForApp(
							"app1",
							"http://app/callback",
							"user@test.com",
							"pw",
							"127.0.0.1",
							"challenge",
							"S256"))
					.thenReturn("auth-code-123");

			MockHttpServletRequest request = new MockHttpServletRequest();
			request.setRemoteAddr("127.0.0.1");

			ResponseEntity<Map<String, String>> result =
					controller.login(
							"app1",
							"http://app/callback",
							"state1",
							"user@test.com",
							"pw",
							"challenge",
							"S256",
							request,
							new MockHttpServletResponse());

			assertEquals(HttpStatus.OK, result.getStatusCode());
			assertTrue(result.getBody().get("redirectUrl").contains("code=auth-code-123"));
			assertTrue(result.getBody().get("redirectUrl").contains("state=state1"));
		}
	}

	@Nested
	@DisplayName("/token 엔드포인트")
	class Token {

		@Test
		@DisplayName("grant_type이 authorization_code가 아니면 400 반환")
		void bad_request_when_invalid_grant_type() {
			ResponseEntity<Map<String, Object>> result =
					controller.token("password", "code", "verifier", "http://x", "client1");

			assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
		}

		@Test
		@DisplayName("유효한 code exchange 시 토큰 반환")
		void exchange_returns_tokens() {
			TokenModel tokenModel =
					TokenModel.builder()
							.accessToken("new-at")
							.refreshToken("new-rt")
							.accessExpiredTime(System.currentTimeMillis() + 3600000)
							.build();
			when(tokenExchangeService.exchange("code123", "verifier", "http://app/callback", "app1"))
					.thenReturn(tokenModel);

			ResponseEntity<Map<String, Object>> result =
					controller.token(
							"authorization_code", "code123", "verifier", "http://app/callback", "app1");

			assertEquals(HttpStatus.OK, result.getStatusCode());
			assertEquals("new-at", result.getBody().get("access_token"));
			assertEquals("new-rt", result.getBody().get("refresh_token"));
			assertEquals("Bearer", result.getBody().get("token_type"));
		}
	}
}
