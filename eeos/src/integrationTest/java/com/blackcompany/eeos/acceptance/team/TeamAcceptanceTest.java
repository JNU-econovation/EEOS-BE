package com.blackcompany.eeos.acceptance.team;

import static com.blackcompany.eeos.acceptance.team.TeamSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.blackcompany.eeos.acceptance.common.AcceptanceTest;
import com.blackcompany.eeos.acceptance.common.TokenProvider;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("팀 인수 테스트")
class TeamAcceptanceTest extends AcceptanceTest {

	private String adminToken;
	private String userToken;

	@BeforeEach
	void setUpToken() {
		adminToken = TokenProvider.createAdminToken(1L);
		userToken = TokenProvider.createUserToken(2L);
	}

	@Test
	@DisplayName("어드민이 팀을 생성할 수 있다")
	void createTeam() {
		// when
		ExtractableResponse<Response> response = 팀_생성_요청(adminToken, "테스트팀");

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(response.jsonPath().getLong("data.teamId")).isNotNull();
	}

	@Test
	@DisplayName("일반 사용자는 팀을 생성할 수 없다")
	void createTeam_denied_for_user() {
		// when
		ExtractableResponse<Response> response = 팀_생성_요청(userToken, "일반유저팀");

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
	}

	@Test
	@DisplayName("활성화된 팀 목록을 조회할 수 있다")
	void getActiveTeams() {
		// given
		팀_생성_요청(adminToken, "조회테스트팀");

		// when
		ExtractableResponse<Response> response = 팀_목록_조회_요청(adminToken, "none");

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	@Test
	@DisplayName("어드민이 팀을 삭제할 수 있다")
	void deleteTeam() {
		// given
		ExtractableResponse<Response> createResponse = 팀_생성_요청(adminToken, "삭제할팀");
		Long teamId = createResponse.jsonPath().getLong("data.teamId");

		// when
		ExtractableResponse<Response> response = 팀_삭제_요청(adminToken, teamId);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	@Test
	@DisplayName("일반 사용자는 팀을 삭제할 수 없다")
	void deleteTeam_denied_for_user() {
		// given
		ExtractableResponse<Response> createResponse = 팀_생성_요청(adminToken, "삭제불가팀");
		Long teamId = createResponse.jsonPath().getLong("data.teamId");

		// when
		ExtractableResponse<Response> response = 팀_삭제_요청(userToken, teamId);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
	}

	@Test
	@DisplayName("중복된 팀 이름으로 생성하면 실패한다")
	void createTeam_duplicate_name() {
		// given
		팀_생성_요청(adminToken, "중복팀");

		// when
		ExtractableResponse<Response> response = 팀_생성_요청(adminToken, "중복팀");

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
	}
}
