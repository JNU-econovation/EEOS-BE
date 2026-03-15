package com.blackcompany.eeos.announcement.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verifyNoInteractions;

import com.blackcompany.eeos.announcement.application.dto.SaveAnnouncementRequest;
import com.blackcompany.eeos.announcement.application.model.AnnouncementModel;
import com.blackcompany.eeos.announcement.application.model.SlackAnnounceEventModel;
import com.blackcompany.eeos.announcement.application.repository.AnnouncementRepository;
import com.blackcompany.eeos.announcement.application.repository.SlackAnnounceEventRepository;
import com.blackcompany.eeos.announcement.application.support.AnnouncementParser;
import com.blackcompany.eeos.announcement.application.support.ParsedAnnouncement;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SaveAnnouncementServiceTest {

	@Mock private SlackAnnounceEventRepository slackAnnounceEventRepository;
	@Mock private AnnouncementRepository announcementRepository;
	@Mock private AnnouncementParser announcementParser;

	@InjectMocks private SaveAnnouncementService saveAnnouncementService;

	@Test
	@DisplayName("threadTs가 null이 아닌 요청은 저장하지 않는다")
	void skip_when_thread_ts_is_not_null() {
		// given
		SaveAnnouncementRequest request =
				SaveAnnouncementRequest.builder()
						.eventId("Ev-100")
						.teamId("T123")
						.channelId("C123")
						.userId("U123")
						.text("스레드 답글입니다.")
						.messageTs("1710000000.000100")
						.threadTs("1710000000.000000")
						.build();

		// when
		saveAnnouncementService.save(request);

		// then
		verifyNoInteractions(slackAnnounceEventRepository);
		verifyNoInteractions(announcementRepository);
	}

	@Test
	@DisplayName("이미 존재하는 eventId는 저장하지 않는다")
	void skip_when_event_id_already_exists() {
		// given
		SaveAnnouncementRequest request =
				SaveAnnouncementRequest.builder()
						.eventId("Ev-200")
						.teamId("T123")
						.channelId("C123")
						.userId("U123")
						.text("중복 이벤트입니다.")
						.messageTs("1710000000.000200")
						.threadTs(null)
						.build();
		given(slackAnnounceEventRepository.existsByEventId("Ev-200")).willReturn(true);

		// when
		saveAnnouncementService.save(request);

		// then
		then(slackAnnounceEventRepository).should(never()).save(any());
		verifyNoInteractions(announcementRepository);
	}

	@Test
	@DisplayName("정상 요청은 SlackAnnounceEventRepository와 AnnouncementRepository에 각 1번씩 save를 호출한다")
	void save_both_repositories_for_valid_request() {
		// given
		SaveAnnouncementRequest request =
				SaveAnnouncementRequest.builder()
						.eventId("Ev-300")
						.teamId("T123")
						.channelId("C123")
						.userId("U123")
						.text("[공지] 정상 공지입니다.")
						.messageTs("1710000000.000300")
						.threadTs(null)
						.build();
		given(slackAnnounceEventRepository.existsByEventId("Ev-300")).willReturn(false);
		given(slackAnnounceEventRepository.save(any()))
				.willReturn(SlackAnnounceEventModel.builder().id(1L).build());
		given(announcementParser.parse("[공지] 정상 공지입니다."))
				.willReturn(new ParsedAnnouncement("공지", "정상 공지입니다.", null));

		// when
		saveAnnouncementService.save(request);

		// then
		then(slackAnnounceEventRepository).should().save(any());
		then(announcementRepository).should().save(any());
	}

	@Test
	@DisplayName("정상 요청 시 파싱된 title과 body가 AnnouncementModel에 저장된다")
	void save_parsed_title_and_body_in_announcement() {
		// given
		SaveAnnouncementRequest request =
				SaveAnnouncementRequest.builder()
						.eventId("Ev-400")
						.teamId("T123")
						.channelId("C123")
						.userId("U123")
						.text("[이벤트 안내] 이번 주 행사 안내드립니다.")
						.messageTs("1710000000.000400")
						.threadTs(null)
						.build();
		given(slackAnnounceEventRepository.existsByEventId("Ev-400")).willReturn(false);
		given(slackAnnounceEventRepository.save(any()))
				.willReturn(SlackAnnounceEventModel.builder().id(2L).build());
		given(announcementParser.parse("[이벤트 안내] 이번 주 행사 안내드립니다."))
				.willReturn(new ParsedAnnouncement("이벤트 안내", "이번 주 행사 안내드립니다.", null));

		// when
		saveAnnouncementService.save(request);

		// then
		ArgumentCaptor<AnnouncementModel> captor = ArgumentCaptor.forClass(AnnouncementModel.class);
		then(announcementRepository).should().save(captor.capture());
		assertThat(captor.getValue().getTitle()).isEqualTo("이벤트 안내");
		assertThat(captor.getValue().getBody()).isEqualTo("이번 주 행사 안내드립니다.");
	}
}
