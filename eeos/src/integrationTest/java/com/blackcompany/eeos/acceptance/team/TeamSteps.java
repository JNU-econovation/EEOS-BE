package com.blackcompany.eeos.acceptance.team;

import static com.blackcompany.eeos.acceptance.common.AcceptanceTestSteps.*;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;

public class TeamSteps {

	private static final String BASE_PATH = "/api/teams";

	public static ExtractableResponse<Response> 팀_생성_요청(String token, String teamName) {
		Map<String, Object> body = new HashMap<>();
		body.put("teamName", teamName);

		return post(BASE_PATH, body, token);
	}

	public static ExtractableResponse<Response> 팀_목록_조회_요청(String token, String programId) {
		String path = String.format("%s?programId=%s", BASE_PATH, programId);
		return get(path, token);
	}

	public static ExtractableResponse<Response> 팀_삭제_요청(String token, Long teamId) {
		return delete(BASE_PATH + "/" + teamId, token);
	}
}
