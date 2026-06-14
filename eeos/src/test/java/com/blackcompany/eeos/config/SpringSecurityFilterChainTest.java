package com.blackcompany.eeos.config;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blackcompany.eeos.auth.application.domain.token.TokenProvider;
import com.blackcompany.eeos.auth.application.domain.token.TokenResolver;
import com.blackcompany.eeos.auth.application.service.AuthService;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("local")
class SecurityFilterChainTest {

	@Autowired private MockMvc mockMvc;

	@MockBean private TokenProvider tokenProvider;
	@MockBean private TokenResolver tokenResolver;
	@MockBean private AuthService authService;

	private String passportHeader(long memberId, String... roles) {
		String roleJson = "[\"" + String.join("\",\"", roles) + "\"]";
		String json =
				String.format(
						"{\"memberId\":%d,\"loginId\":\"user%d\",\"name\":\"테스터\",\"generation\":30,\"status\":\"AM\",\"roles\":%s,\"issuedAt\":\"%s\",\"expiresAt\":\"%s\"}",
						memberId, memberId, roleJson, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
		return Base64.getEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));
	}

	@Nested
	@DisplayName("1. 인증 필요 없는 엔드포인트")
	class NonAuthorizedEndpoints {

		@Test
		@DisplayName("인증 없이 /api/auth/login 접근 가능 (400 또는 303 반환)")
		void loginShouldReturn200() throws Exception {
			mockMvc
					.perform(
							post("/api/auth/login")
									.contentType(MediaType.APPLICATION_FORM_URLENCODED)
									.param("client_id", "unknown")
									.param("redirect_uri", "http://test.com")
									.param("state", "test")
									.param("email", "user")
									.param("password", "password"))
					.andExpect(status().is(org.hamcrest.Matchers.not(401)))
					.andExpect(status().is(org.hamcrest.Matchers.not(403)));
		}

		@Test
		void healthCheckShouldReturn200() throws Exception {
			mockMvc.perform(get("/api/health-check")).andExpect(status().isOk());
		}
	}

	@Nested
	@DisplayName("2-1. 인증이 필요한 엔드포인트 - 일반 유저")
	class UserEndpoints {

		@Test
		@DisplayName("[일반유저] Passport 헤더가 없으면 401 반환")
		void 일반유저_패스포트_없으면_401반환() throws Exception {
			mockMvc.perform(get("/api/programs")).andExpect(status().isUnauthorized());
		}

		@Test
		@DisplayName("[일반유저] Passport 헤더가 있으면 200 반환")
		void 일반유저_올바른_패스포트가_있으면_200응답() throws Exception {
			mockMvc
					.perform(
							get("/api/members?activeStatus=all")
									.header("X-User-Passport", passportHeader(1L, "USER")))
					.andExpect(status().isOk());
		}

		@Test
		@DisplayName("[일반유저] 일반 유저 권한은 관리자 API에 접근 불가능_1")
		void 일반유저_패스포트로_관리자_API_접근시_403응답_1() throws Exception {
			mockMvc
					.perform(post("/api/programs").header("X-User-Passport", passportHeader(1L, "USER")))
					.andExpect(status().isForbidden());
		}

		@Test
		@DisplayName("[일반유저] 일반 유저 권한은 관리자 API에 접근 불가능_2")
		void 일반유저_패스포트로_관리자_API_접근시_403응답_2() throws Exception {
			mockMvc
					.perform(delete("/api/programs").header("X-User-Passport", passportHeader(1L, "USER")))
					.andExpect(status().isForbidden());
		}

		@Test
		@DisplayName("[일반유저] 일반 유저 권한은 관리자 API에 접근 불가능_3")
		void 일반유저_패스포트로_관리자_API_접근시_403응답_3() throws Exception {
			mockMvc
					.perform(delete("/api/members/1").header("X-User-Passport", passportHeader(1L, "USER")))
					.andExpect(status().isForbidden());
		}

		@Test
		@DisplayName("[일반유저] 일반 유저 권한은 관리자 API에 접근 불가능_4")
		void 일반유저_패스포트로_관리자_API_접근시_403응답_4() throws Exception {
			mockMvc
					.perform(
							put("/api/members/activeStatus/1")
									.header("X-User-Passport", passportHeader(1L, "USER")))
					.andExpect(status().isForbidden());
		}

		@Test
		@DisplayName("[일반유저] 일반 유저 권한은 관리자 API에 접근 불가능_5")
		void 일반유저_패스포트로_관리자_API_접근시_403응답_5() throws Exception {
			mockMvc
					.perform(post("/api/teams").header("X-User-Passport", passportHeader(1L, "USER")))
					.andExpect(status().isForbidden());
		}

		@Test
		@DisplayName("[일반유저] 일반 유저 권한은 관리자 API에 접근 불가능_6")
		void 일반유저_패스포트로_관리자_API_접근시_403응답_6() throws Exception {
			mockMvc
					.perform(delete("/api/teams/1").header("X-User-Passport", passportHeader(1L, "USER")))
					.andExpect(status().isForbidden());
		}

		@Test
		@DisplayName("[일반유저] 일반 유저 권한은 관리자 API에 접근 불가능_7")
		void 일반유저_패스포트로_관리자_API_접근시_403응답_7() throws Exception {
			mockMvc
					.perform(get("/api/admin/test").header("X-User-Passport", passportHeader(1L, "USER")))
					.andExpect(status().isForbidden());
		}
	}

	@Nested
	@DisplayName("2-2. 인증이 필요한 엔드포인트 - 관리자")
	class AdminEndPoint {

		@Test
		@DisplayName("[관리자] 관리자 권한은 관리자 API에 접근 가능_1")
		void 관리자_패스포트로_관리자_API_접근시_200응답() throws Exception {
			mockMvc
					.perform(get("/api/admin/test").header("X-User-Passport", passportHeader(1L, "ADMIN")))
					.andExpect(status().isOk());
		}
	}

	@Nested
	@DisplayName("3. 존재하지 않는 엔드포인트(UnknownEndpointFilter)")
	class UnknownEndpoint {

		@Test
		void nonExistentShouldReturn404() throws Exception {
			mockMvc.perform(get("/api/does-not-exist")).andExpect(status().isNotFound());
		}
	}
}
