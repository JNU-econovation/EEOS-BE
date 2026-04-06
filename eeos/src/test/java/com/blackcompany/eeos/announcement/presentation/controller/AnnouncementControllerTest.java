package com.blackcompany.eeos.announcement.presentation.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blackcompany.eeos.announcement.application.dto.AnnouncementResponse;
import com.blackcompany.eeos.announcement.application.dto.GetAnnouncementsResponse;
import com.blackcompany.eeos.announcement.application.usecase.GetAnnouncementsUsecase;
import com.blackcompany.eeos.auth.application.domain.token.TokenResolver;
import com.blackcompany.eeos.auth.presentation.support.TokenExtractor;
import com.blackcompany.eeos.common.utils.DateConverter;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AnnouncementController.class)
@AutoConfigureMockMvc(addFilters = false)
class AnnouncementControllerTest {

	@Autowired private MockMvc mockMvc;

	@MockBean private GetAnnouncementsUsecase getAnnouncementsUsecase;
	@MockBean private TokenResolver tokenResolver;

	@MockBean(name = "header")
	private TokenExtractor headerTokenExtractor;

	@MockBean(name = "cookie")
	private TokenExtractor cookieTokenExtractor;

	@Test
	@DisplayName("인증된 사용자로 GET /api/announcements 요청 시 200 OK와 공지 목록을 반환한다")
	void get_announcements_returns_200_with_list() throws Exception {
		// given
		GetAnnouncementsResponse response =
				GetAnnouncementsResponse.builder()
						.announcements(
								List.of(
										AnnouncementResponse.builder()
												.id(1L)
												.title("이벤트 안내")
												.body("이번 주 행사 안내드립니다.")
												.createdDate(
														DateConverter.toMillis(LocalDateTime.of(2026, 3, 15, 10, 0, 0)))
												.build(),
										AnnouncementResponse.builder()
												.id(2L)
												.title(null)
												.body("제목 없는 공지입니다.")
												.createdDate(DateConverter.toMillis(LocalDateTime.of(2026, 3, 14, 9, 0, 0)))
												.build()))
						.build();
		given(getAnnouncementsUsecase.getAnnouncements()).willReturn(response);

		// when & then
		mockMvc
				.perform(
						get("/api/announcements")
								.contentType(MediaType.APPLICATION_JSON)
								.header(HttpHeaders.AUTHORIZATION, "Bearer valid-token"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.announcements").isArray())
				.andExpect(jsonPath("$.data.announcements[0].id").value(1))
				.andExpect(jsonPath("$.data.announcements[0].title").value("이벤트 안내"))
				.andExpect(jsonPath("$.data.announcements[0].body").value("이번 주 행사 안내드립니다."))
				.andExpect(jsonPath("$.data.announcements[1].title").isEmpty());
	}
}
