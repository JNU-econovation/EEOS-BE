package com.blackcompany.eeos.config;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blackcompany.eeos.auth.application.domain.token.JwtTestUtil;
import com.blackcompany.eeos.auth.application.domain.token.TokenProvider;
import com.blackcompany.eeos.auth.application.domain.token.TokenResolver;
import com.blackcompany.eeos.auth.application.service.AuthService;
import com.blackcompany.eeos.member.application.model.ActiveStatus;
import com.blackcompany.eeos.member.application.model.MemberModel;
import com.blackcompany.eeos.member.fixture.MemberFixture;
import java.sql.Date;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
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

	@Nested
	@DisplayName("1. 인증 필요 없는 엔드포인트")
	class NonAuthorizedEndpoints {

		private final String id = "user";
		private final String password = "password";

		private final MemberModel testMember = MemberFixture.멤버_모델(1L, ActiveStatus.AM);

		@Test
		void loginShouldReturn200() throws Exception {
			// given
			given(authService.authenticate(id, password)).willReturn(testMember);

			// when
			mockMvc
					.perform(
							post("/api/auth/login")
									.contentType(MediaType.APPLICATION_JSON)
									.content(String.format("{\"id\":\"%s\",\"password\":\"%s\"}", id, password)))
					.andExpect(status().isCreated()); // then
		}

		@Test
		void healthCheckShouldReturn200() throws Exception {
			mockMvc.perform(get("/api/health-check")).andExpect(status().isOk());
		}
	}

	@Nested
	@DisplayName("2-1. 인증이 필요한 엔드포인트 - 일반 유저")
	class UserEndpoints {

		private final String VALID_JWT = JwtTestUtil.createToken(1L, "USER");

		@BeforeEach
		void setAccessToken() {
			given(tokenProvider.createAccessToken(any(), any())).willReturn(VALID_JWT);

			given(tokenResolver.getUserDataByAccessToken(VALID_JWT)).willReturn(1L);

			given(tokenResolver.getExpiredDateByAccessToken(VALID_JWT))
					.willReturn(Date.from(Instant.now()).getTime());
		}

		@Test
		@DisplayName("[일반유저] 토큰이 존재하지 않으면 401 반환")
		void 일반유저_토큰이_존재하지_않으면_401반환() throws Exception {

			mockMvc.perform(get("/api/programs")).andExpect(status().isUnauthorized());
		}

		@Test
		@DisplayName("[일반유저] 토큰이 있으면 200 반환")
		void 일반유저_올바른_토큰이_있으면_200응답() throws Exception {
			mockMvc
					.perform(get("/api/members?activeStatus=all").header("Authorization", bearerToken()))
					.andExpect(status().isOk());
		}

		@Test
		@DisplayName("[일반유저] 일반 유저 권한은 관리자 API에 접근 불가능_1")
		void 일반유저_토큰으로_관리자_API_접근시_403응답_1() throws Exception {
			mockMvc
					.perform(post("/api/programs").header(HttpHeaders.AUTHORIZATION, bearerToken()))
					.andExpect(status().isForbidden());
		}

		@Test
		@DisplayName("[일반유저] 일반 유저 권한은 관리자 API에 접근 불가능_2")
		void 일반유저_토큰으로_관리자_API_접근시_403응답_2() throws Exception {
			mockMvc
					.perform(delete("/api/programs").header(HttpHeaders.AUTHORIZATION, bearerToken()))
					.andExpect(status().isForbidden());
		}

		@Test
		@DisplayName("[일반유저] 일반 유저 권한은 관리자 API에 접근 불가능_3")
		void 일반유저_토큰으로_관리자_API_접근시_403응답_1_3() throws Exception {
			mockMvc
					.perform(delete("/api/members/1").header(HttpHeaders.AUTHORIZATION, bearerToken()))
					.andExpect(status().isForbidden());
		}

		@Test
		@DisplayName("[일반유저] 일반 유저 권한은 관리자 API에 접근 불가능_4")
		void 일반유저_토큰으로_관리자_API_접근시_403응답_1_4() throws Exception {
			mockMvc
					.perform(
							put("/api/members/activeStatus/1").header(HttpHeaders.AUTHORIZATION, bearerToken()))
					.andExpect(status().isForbidden());
		}

		@Test
		@DisplayName("[일반유저] 일반 유저 권한은 관리자 API에 접근 불가능_5")
		void 일반유저_토큰으로_관리자_API_접근시_403응답_1_5() throws Exception {
			mockMvc
					.perform(post("/api/teams").header(HttpHeaders.AUTHORIZATION, bearerToken()))
					.andExpect(status().isForbidden());
		}

		@Test
		@DisplayName("[일반유저] 일반 유저 권한은 관리자 API에 접근 불가능_6")
		void 일반유저_토큰으로_관리자_API_접근시_403응답_1_6() throws Exception {
			mockMvc
					.perform(delete("/api/teams/1").header(HttpHeaders.AUTHORIZATION, bearerToken()))
					.andExpect(status().isForbidden());
		}

		@Test
		@DisplayName("[일반유저] 일반 유저 권한은 관리자 API에 접근 불가능_7")
		void 일반유저_토큰으로_관리자_API_접근시_403응답_1_7() throws Exception {
			mockMvc
					.perform(get("/api/admin/test").header(HttpHeaders.AUTHORIZATION, bearerToken()))
					.andExpect(status().isForbidden());
		}

		private String bearerToken() {
			return String.format("Bearer %s", VALID_JWT);
		}
	}

	@Nested
	@DisplayName("2-2. 인증이 필요한 엔드포인트 - 관리자")
	class AdminEndPoint {}

	@Nested
	@DisplayName("3. 존재하지 않는 엔드포인트(UnknownEndpointFilter)")
	class UnknownEndpoint {
		@Test
		void nonExistentShouldReturn404() throws Exception {
			mockMvc.perform(get("/api/does-not-exist")).andExpect(status().isNotFound());
		}
	}
}
