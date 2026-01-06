package com.blackcompany.eeos.acceptance.calendar;

import static com.blackcompany.eeos.acceptance.common.AcceptanceTestSteps.*;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;

public class CalendarSteps {

	private static final String BASE_PATH = "/api/calendars";

	public static ExtractableResponse<Response> 캘린더_생성_요청(
			String token, String title, String type, Long startAt, Long endAt) {
		Map<String, Object> body = new HashMap<>();
		body.put("title", title);
		body.put("type", type);
		body.put("url", "https://example.com");
		body.put("startAt", startAt);
		body.put("endAt", endAt);

		return post(BASE_PATH, body, token);
	}

	public static ExtractableResponse<Response> 캘린더_목록_조회_요청(
			String token, int year, int month) {
		String path = String.format("%s?year=%d&month=%d", BASE_PATH, year, month);
		return get(path, token);
	}

	public static ExtractableResponse<Response> 캘린더_목록_조회_요청_일별(
			String token, int year, int month, int date, int duration) {
		String path =
				String.format(
						"%s?year=%d&month=%d&date=%d&duration=%d", BASE_PATH, year, month, date, duration);
		return get(path, token);
	}

	public static ExtractableResponse<Response> 캘린더_수정_요청(
			String token, Long calendarId, String title, String type, Long startAt, Long endAt) {
		Map<String, Object> body = new HashMap<>();
		body.put("title", title);
		body.put("type", type);
		body.put("url", "https://updated.com");
		body.put("startAt", startAt);
		body.put("endAt", endAt);

		return put(BASE_PATH + "/" + calendarId, body, token);
	}

	public static ExtractableResponse<Response> 캘린더_삭제_요청(String token, Long calendarId) {
		return delete(BASE_PATH + "/" + calendarId, token);
	}

	public static ExtractableResponse<Response> D_DAY_캘린더_조회_요청(String token, int measure) {
		String path = String.format("%s/d-day?measure=%d", BASE_PATH, measure);
		return get(path, token);
	}
}
