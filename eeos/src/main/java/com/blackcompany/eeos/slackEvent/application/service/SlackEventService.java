package com.blackcompany.eeos.slackEvent.application.service;

import com.blackcompany.eeos.slackEvent.application.dto.EeosSlackMessageForwardRequest;
import com.blackcompany.eeos.slackEvent.application.dto.SlackEventAckResponse;
import com.blackcompany.eeos.slackEvent.application.dto.SlackEventEnvelopeRequest;
import com.blackcompany.eeos.slackEvent.application.dto.SlackEventEnvelopeRequest.SlackInnerEvent;
import com.blackcompany.eeos.slackEvent.application.exception.InvalidSlackEventIdException;
import com.blackcompany.eeos.slackEvent.application.exception.SlackForwardFailedException;
import com.blackcompany.eeos.slackEvent.application.repository.SlackEventDedupRepository;
import com.blackcompany.eeos.slackEvent.application.usecase.HandleSlackEventUsecase;
import com.blackcompany.eeos.slackEvent.infra.client.EeosSlackForwardApiClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SlackEventService implements HandleSlackEventUsecase {

	private static final String STATUS_FORWARDED = "FORWARDED";
	private static final String STATUS_IGNORED_UNSUPPORTED = "IGNORED_UNSUPPORTED";
	private static final String STATUS_IGNORED_FILTERED = "IGNORED_FILTERED";
	private static final String STATUS_IGNORED_DUPLICATE = "IGNORED_DUPLICATE";
	private static final String MESSAGE_EVENT_TYPE = "message";

	private final SlackEventDedupRepository dedupRepository;
	private final EeosSlackForwardApiClient forwardApiClient;
	private final String forwardApiKey;

	public SlackEventService(
			SlackEventDedupRepository dedupRepository,
			EeosSlackForwardApiClient forwardApiClient,
			@Value("${slack.event.forward.api-key:}") String forwardApiKey) {
		this.dedupRepository = dedupRepository;
		this.forwardApiClient = forwardApiClient;
		this.forwardApiKey = forwardApiKey;
	}

	@Override
	public SlackEventAckResponse handle(SlackEventEnvelopeRequest request) {
		SlackInnerEvent event = request.getEvent();
		String eventId = request.getEventId();
		validateEventId(eventId);

		// Event 무시
		if (event == null || !MESSAGE_EVENT_TYPE.equals(event.getType())) {
			return SlackEventAckResponse.of(eventId, STATUS_IGNORED_UNSUPPORTED);
		}

		if (event.getSubtype() != null) {
			return SlackEventAckResponse.of(eventId, STATUS_IGNORED_FILTERED);
		}
		// 이미 처리된 event 인지 검증
		if (dedupRepository.isProcessed(eventId)) {
			return SlackEventAckResponse.of(eventId, STATUS_IGNORED_DUPLICATE);
		}
		// 처리되지 않았으면 Lock 시도
		if (!dedupRepository.tryLock(eventId)) {
			return SlackEventAckResponse.of(eventId, STATUS_IGNORED_DUPLICATE);
		}

		try {
			// internal call 수행
			forwardApiClient.forward(forwardApiKey, toForwardRequest(request));
			// event processed 처리
			dedupRepository.markProcessed(eventId);
			return SlackEventAckResponse.of(eventId, STATUS_FORWARDED);
		} catch (Exception e) {
			log.error("Slack 이벤트 전달 실패. eventId={}", eventId, e);
			throw new SlackForwardFailedException(eventId, e);
		} finally {
			// lock 해제
			dedupRepository.unlock(eventId);
		}
	}

	// eventId 검증
	private void validateEventId(String eventId) {
		if (eventId == null || eventId.isBlank()) {
			throw new InvalidSlackEventIdException();
		}
	}

	private EeosSlackMessageForwardRequest toForwardRequest(SlackEventEnvelopeRequest request) {
		SlackInnerEvent event = request.getEvent();
		return EeosSlackMessageForwardRequest.builder()
				.eventId(request.getEventId())
				.teamId(request.getTeamId())
				.channelId(event.getChannel())
				.userId(event.getUser())
				.text(event.getText())
				.messageTs(event.getTs())
				.threadTs(event.getThreadTs())
				.build();
	}
}
