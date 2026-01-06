package com.blackcompany.eeos.acceptance.program;

import static com.blackcompany.eeos.acceptance.common.AcceptanceTestSteps.*;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgramSteps {

	private static final String BASE_PATH = "/api/programs";
	private static final String GITHUB_URL = "https://github.com/JNU-econovation/test";

	public static ExtractableResponse<Response> 프로그램_생성_요청(
			String token, String title, String category, String type) {
		Timestamp futureDate = Timestamp.valueOf(LocalDate.now().plusDays(7).atStartOfDay());

		Map<String, Object> body = new HashMap<>();
		body.put("title", title);
		body.put("deadLine", futureDate.getTime());
		body.put("content", "테스트 프로그램 내용");
		body.put("category", category);
		body.put("type", type);
		body.put("teams", List.of());
		body.put("programGithubUrl", GITHUB_URL);
		body.put("members", List.of());

		return post(BASE_PATH, body, token);
	}

	public static ExtractableResponse<Response> 프로그램_조회_요청(String token, Long programId) {
		return get(BASE_PATH + "/" + programId, token);
	}

	public static ExtractableResponse<Response> 프로그램_목록_조회_요청(
			String token, String category, String status, int size, int page) {
		String path =
				String.format(
						"%s?category=%s&programStatus=%s&size=%d&page=%d",
						BASE_PATH, category, status, size, page);
		return get(path, token);
	}

	public static ExtractableResponse<Response> 프로그램_수정_요청(
			String token, Long programId, String title, String category, String type) {
		Timestamp futureDate = Timestamp.valueOf(LocalDate.now().plusDays(14).atStartOfDay());

		Map<String, Object> body = new HashMap<>();
		body.put("title", title);
		body.put("deadLine", futureDate.getTime());
		body.put("content", "수정된 프로그램 내용");
		body.put("category", category);
		body.put("type", type);
		body.put("programGithubUrl", GITHUB_URL);
		body.put("members", List.of());

		return patch(BASE_PATH + "/" + programId, body, token);
	}

	public static ExtractableResponse<Response> 프로그램_삭제_요청(String token, Long programId) {
		return delete(BASE_PATH + "/" + programId, token);
	}

	public static ExtractableResponse<Response> 프로그램_접근권한_조회_요청(String token, Long programId) {
		return get(BASE_PATH + "/" + programId + "/accessRight", token);
	}
}
