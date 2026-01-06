package com.blackcompany.eeos.acceptance.calendar;

import static com.blackcompany.eeos.acceptance.calendar.CalendarSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.blackcompany.eeos.acceptance.common.AcceptanceTest;
import com.blackcompany.eeos.acceptance.common.TokenProvider;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("캘린더 인수 테스트")
class CalendarAcceptanceTest extends AcceptanceTest {

	private String adminToken;
	private Long startAt;
	private Long endAt;

	@BeforeEach
	void setUpToken() {
		adminToken = TokenProvider.createAdminToken(1L);
		LocalDateTime now = LocalDateTime.now().plusDays(1);
		startAt = now.toInstant(ZoneOffset.UTC).toEpochMilli();
		endAt = now.plusHours(2).toInstant(ZoneOffset.UTC).toEpochMilli();
	}

	@Test
	@DisplayName("캘린더를 생성할 수 있다")
	void createCalendar() {
		// when
		ExtractableResponse<Response> response =
				캘린더_생성_요청(adminToken, "테스트 일정", "ETC", startAt, endAt);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(response.jsonPath().getLong("data")).isNotNull();
	}

	@Test
	@DisplayName("월별 캘린더 목록을 조회할 수 있다")
	void getCalendarsByMonth() {
		// given
		캘린더_생성_요청(adminToken, "1월 일정", "ETC", startAt, endAt);

		// when
		int year = LocalDateTime.now().getYear();
		int month = LocalDateTime.now().getMonthValue();
		ExtractableResponse<Response> response = 캘린더_목록_조회_요청(adminToken, year, month);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	@Test
	@DisplayName("일별 캘린더 목록을 조회할 수 있다")
	void getCalendarsByDate() {
		// given
		캘린더_생성_요청(adminToken, "일별 조회 테스트", "ETC", startAt, endAt);

		// when
		LocalDateTime now = LocalDateTime.now();
		ExtractableResponse<Response> response =
				캘린더_목록_조회_요청_일별(
						adminToken, now.getYear(), now.getMonthValue(), now.getDayOfMonth(), 7);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	@Test
	@DisplayName("캘린더를 수정할 수 있다")
	void updateCalendar() {
		// given
		ExtractableResponse<Response> createResponse =
				캘린더_생성_요청(adminToken, "수정 전 일정", "ETC", startAt, endAt);
		Long calendarId = createResponse.jsonPath().getLong("data");

		// when
		ExtractableResponse<Response> response =
				캘린더_수정_요청(adminToken, calendarId, "수정 후 일정", "ETC", startAt, endAt);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(response.jsonPath().getLong("data")).isEqualTo(calendarId);
	}

	@Test
	@DisplayName("캘린더를 삭제할 수 있다")
	void deleteCalendar() {
		// given
		ExtractableResponse<Response> createResponse =
				캘린더_생성_요청(adminToken, "삭제할 일정", "ETC", startAt, endAt);
		Long calendarId = createResponse.jsonPath().getLong("data");

		// when
		ExtractableResponse<Response> response = 캘린더_삭제_요청(adminToken, calendarId);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	@Test
	@DisplayName("D-Day 기준으로 캘린더를 조회할 수 있다")
	void getCalendarsForDDay() {
		// given
		캘린더_생성_요청(adminToken, "D-Day 테스트 일정", "ETC", startAt, endAt);

		// when
		ExtractableResponse<Response> response = D_DAY_캘린더_조회_요청(adminToken, 7);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}
}
