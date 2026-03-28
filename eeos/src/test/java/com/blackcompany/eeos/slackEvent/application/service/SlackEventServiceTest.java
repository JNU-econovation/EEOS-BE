package com.blackcompany.eeos.slackEvent.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verifyNoInteractions;

import com.blackcompany.eeos.slackEvent.application.dto.EeosSlackMessageForwardRequest;
import com.blackcompany.eeos.slackEvent.application.dto.SlackEventAckResponse;
import com.blackcompany.eeos.slackEvent.application.dto.SlackEventRequest;
import com.blackcompany.eeos.slackEvent.application.exception.InvalidSlackEventIdException;
import com.blackcompany.eeos.slackEvent.application.exception.SlackForwardFailedException;
import com.blackcompany.eeos.slackEvent.application.repository.SlackEventDedupRepository;
import com.blackcompany.eeos.slackEvent.infra.client.EeosSlackForwardApiClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SlackEventServiceTest {

	@Mock private SlackEventDedupRepository dedupRepository;
	@Mock private EeosSlackForwardApiClient forwardApiClient;

	@InjectMocks private SlackEventService slackEventService;

	@Test
	@DisplayName("message 타입이 아니면 내부 API 호출 없이 무시한다")
	void ignore_when_not_message_event() {
		// given
		SlackEventRequest request = createEventCallback("Ev-1", "reaction_added", null);

		// when
		SlackEventAckResponse response = slackEventService.handle(request);

		// then
		assertThat(response.getStatus()).isEqualTo("IGNORED_UNSUPPORTED");
		verifyNoInteractions(forwardApiClient);
	}

	@Test
	@DisplayName("message 이벤트여도 subtype이 있으면 내부 API 호출 없이 무시한다")
	void ignore_when_message_has_subtype() {
		// given
		SlackEventRequest request = createEventCallback("Ev-2", "message", "bot_message");

		// when
		SlackEventAckResponse response = slackEventService.handle(request);

		// then
		assertThat(response.getStatus()).isEqualTo("IGNORED_FILTERED");
		verifyNoInteractions(forwardApiClient);
	}

	@Test
	@DisplayName("이미 처리된 event_id는 중복으로 무시한다")
	void ignore_when_event_already_processed() {
		// given
		SlackEventRequest request = createEventCallback("Ev-3", "message", null);
		given(dedupRepository.isProcessed("Ev-3")).willReturn(true);

		// when
		SlackEventAckResponse response = slackEventService.handle(request);

		// then
		assertThat(response.getStatus()).isEqualTo("IGNORED_DUPLICATE");
		then(dedupRepository).should(never()).tryLock(any());
		verifyNoInteractions(forwardApiClient);
	}

	@Test
	@DisplayName("처리 락 획득에 실패하면 중복으로 무시한다")
	void ignore_when_lock_not_acquired() {
		// given
		SlackEventRequest request = createEventCallback("Ev-4", "message", null);
		given(dedupRepository.isProcessed("Ev-4")).willReturn(false);
		given(dedupRepository.tryLock("Ev-4")).willReturn(false);

		// when
		SlackEventAckResponse response = slackEventService.handle(request);

		// then
		assertThat(response.getStatus()).isEqualTo("IGNORED_DUPLICATE");
		verifyNoInteractions(forwardApiClient);
	}

	@Test
	@DisplayName("유효한 메시지 이벤트는 내부 API로 전달하고 처리 완료를 기록한다")
	void forward_when_valid_message_event() {
		// given
		SlackEventRequest request = createEventCallback("Ev-5", "message", null);
		given(dedupRepository.isProcessed("Ev-5")).willReturn(false);
		given(dedupRepository.tryLock("Ev-5")).willReturn(true);

		// when
		SlackEventAckResponse response = slackEventService.handle(request);

		// then
		assertThat(response.getStatus()).isEqualTo("FORWARDED");

		ArgumentCaptor<EeosSlackMessageForwardRequest> captor =
				ArgumentCaptor.forClass(EeosSlackMessageForwardRequest.class);
		then(forwardApiClient).should().forward(any(), captor.capture());
		assertThat(captor.getValue().getEventId()).isEqualTo("Ev-5");
		assertThat(captor.getValue().getChannelId()).isEqualTo("C123");
		assertThat(captor.getValue().getText()).isEqualTo("안녕하세요");

		then(dedupRepository).should().markProcessed("Ev-5");
		then(dedupRepository).should().unlock("Ev-5");
	}

	@Test
	@DisplayName("내부 API 전달 중 예외가 발생하면 SlackForwardFailedException을 던지고 락을 해제한다")
	void throw_when_forward_failed() {
		// given
		SlackEventRequest request = createEventCallback("Ev-6", "message", null);
		given(dedupRepository.isProcessed("Ev-6")).willReturn(false);
		given(dedupRepository.tryLock("Ev-6")).willReturn(true);
		willThrow(new RuntimeException("forward fail")).given(forwardApiClient).forward(any(), any());

		// when & then
		assertThatThrownBy(() -> slackEventService.handle(request))
				.isInstanceOf(SlackForwardFailedException.class);
		then(dedupRepository).should().unlock("Ev-6");
		then(dedupRepository).should(never()).markProcessed(any());
	}

	@Test
	@DisplayName("event_id가 비어 있으면 InvalidSlackEventIdException을 던진다")
	void throw_when_event_id_is_blank() {
		// given
		SlackEventRequest request = createEventCallback(" ", "message", null);

		// when & then
		assertThatThrownBy(() -> slackEventService.handle(request))
				.isInstanceOf(InvalidSlackEventIdException.class);
		verifyNoInteractions(forwardApiClient);
	}

	private SlackEventRequest createEventCallback(
			String eventId, String eventType, String subtype) {
		return SlackEventRequest.builder()
				.type("event_callback")
				.eventId(eventId)
				.eventTime(1710000000L)
				.teamId("T123")
				.event(
						SlackEventRequest.SlackMessageInfo.builder()
								.type(eventType)
								.channel("C123")
								.user("U123")
								.text("안녕하세요")
								.ts("1710000000.000100")
								.threadTs("1710000000.000100")
								.subtype(subtype)
								.build())
				.build();
	}
}
