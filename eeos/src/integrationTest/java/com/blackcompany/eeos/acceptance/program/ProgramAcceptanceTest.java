package com.blackcompany.eeos.acceptance.program;

import static com.blackcompany.eeos.acceptance.program.ProgramSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.blackcompany.eeos.acceptance.common.AcceptanceTest;
import com.blackcompany.eeos.acceptance.common.TokenProvider;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("프로그램 인수 테스트")
class ProgramAcceptanceTest extends AcceptanceTest {

	private String adminToken;
	private String userToken;
	private String anotherAdminToken;

	@BeforeEach
	void setUpToken() {
		adminToken = TokenProvider.createAdminToken(1L);
		userToken = TokenProvider.createUserToken(2L);
		anotherAdminToken = TokenProvider.createAdminToken(3L);
	}

	@Test
	@DisplayName("어드민이 프로그램을 생성할 수 있다")
	void createProgram() {
		// when
		ExtractableResponse<Response> response =
				프로그램_생성_요청(adminToken, "테스트 프로그램", "weekly", "demand");

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(response.jsonPath().getLong("data.programId")).isNotNull();
	}

	@Test
	@DisplayName("일반 사용자는 프로그램을 생성할 수 없다")
	void createProgram_denied_for_user() {
		// when
		ExtractableResponse<Response> response =
				프로그램_생성_요청(userToken, "일반유저 프로그램", "weekly", "demand");

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
	}

	@Test
	@DisplayName("프로그램을 조회할 수 있다")
	void getProgram() {
		// given
		ExtractableResponse<Response> createResponse =
				프로그램_생성_요청(adminToken, "조회 테스트 프로그램", "weekly", "demand");
		Long programId = createResponse.jsonPath().getLong("data.programId");

		// when
		ExtractableResponse<Response> response = 프로그램_조회_요청(adminToken, programId);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(response.jsonPath().getLong("data.programId")).isEqualTo(programId);
	}

	@Test
	@DisplayName("프로그램 목록을 조회할 수 있다")
	void getPrograms() {
		// given
		프로그램_생성_요청(adminToken, "목록조회 테스트1", "weekly", "demand");
		프로그램_생성_요청(adminToken, "목록조회 테스트2", "weekly", "demand");

		// when
		ExtractableResponse<Response> response =
				프로그램_목록_조회_요청(adminToken, "weekly", "active", 10, 0);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	@Test
	@DisplayName("작성자가 프로그램을 수정할 수 있다")
	void updateProgram() {
		// given
		ExtractableResponse<Response> createResponse =
				프로그램_생성_요청(adminToken, "수정 전 프로그램", "weekly", "demand");
		Long programId = createResponse.jsonPath().getLong("data.programId");

		// when
		ExtractableResponse<Response> response =
				프로그램_수정_요청(adminToken, programId, "수정 후 프로그램", "weekly", "demand");

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	@Test
	@DisplayName("작성자가 아니면 프로그램을 수정할 수 없다")
	void updateProgram_denied_for_non_writer() {
		// given
		ExtractableResponse<Response> createResponse =
				프로그램_생성_요청(adminToken, "다른 사람 프로그램", "weekly", "demand");
		Long programId = createResponse.jsonPath().getLong("data.programId");

		// when
		ExtractableResponse<Response> response =
				프로그램_수정_요청(anotherAdminToken, programId, "수정 시도", "weekly", "demand");

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
	}

	@Test
	@DisplayName("작성자가 프로그램을 삭제할 수 있다")
	void deleteProgram() {
		// given
		ExtractableResponse<Response> createResponse =
				프로그램_생성_요청(adminToken, "삭제할 프로그램", "weekly", "demand");
		Long programId = createResponse.jsonPath().getLong("data.programId");

		// when
		ExtractableResponse<Response> response = 프로그램_삭제_요청(adminToken, programId);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	@Test
	@DisplayName("작성자가 아니면 프로그램을 삭제할 수 없다")
	void deleteProgram_denied_for_non_writer() {
		// given
		ExtractableResponse<Response> createResponse =
				프로그램_생성_요청(adminToken, "삭제 불가 프로그램", "weekly", "demand");
		Long programId = createResponse.jsonPath().getLong("data.programId");

		// when
		ExtractableResponse<Response> response = 프로그램_삭제_요청(anotherAdminToken, programId);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
	}

	@Test
	@DisplayName("프로그램 접근 권한을 조회할 수 있다")
	void getAccessRight() {
		// given
		ExtractableResponse<Response> createResponse =
				프로그램_생성_요청(adminToken, "접근권한 테스트", "weekly", "demand");
		Long programId = createResponse.jsonPath().getLong("data.programId");

		// when
		ExtractableResponse<Response> response = 프로그램_접근권한_조회_요청(adminToken, programId);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(response.jsonPath().getString("data.accessRight")).isEqualTo("edit");
	}

	@Test
	@DisplayName("작성자가 아니면 읽기 전용 접근 권한을 갖는다")
	void getAccessRight_readOnly_for_non_writer() {
		// given
		ExtractableResponse<Response> createResponse =
				프로그램_생성_요청(adminToken, "읽기전용 테스트", "weekly", "demand");
		Long programId = createResponse.jsonPath().getLong("data.programId");

		// when
		ExtractableResponse<Response> response = 프로그램_접근권한_조회_요청(userToken, programId);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(response.jsonPath().getString("data.accessRight")).isEqualTo("readOnly");
	}
}
