package com.blackcompany.eeos.announcement.application.service;

import com.blackcompany.eeos.announcement.application.dto.SaveAnnouncementRequest;
import com.blackcompany.eeos.announcement.application.model.AnnouncementModel;
import com.blackcompany.eeos.announcement.application.model.SlackAnnounceEventModel;
import com.blackcompany.eeos.announcement.application.repository.AnnouncementRepository;
import com.blackcompany.eeos.announcement.application.repository.SlackAnnounceEventRepository;
import com.blackcompany.eeos.announcement.application.support.SlackMessageParser;
import com.blackcompany.eeos.announcement.application.usecase.SaveAnnouncementUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SaveAnnouncementService implements SaveAnnouncementUsecase {

	private final SlackAnnounceEventRepository slackAnnounceEventRepository;
	private final AnnouncementRepository announcementRepository;
	private final SlackMessageParser slackMessageParser;

	@Override
	@Transactional
	public void save(SaveAnnouncementRequest request) {
		// 스레드 답글은 저장하지 않음
		if (request.getThreadTs() != null) {
			return;
		}

		// 중복 eventId는 저장하지 않음
		if (slackAnnounceEventRepository.existsByEventId(request.getEventId())) {
			return;
		}

		SlackAnnounceEventModel eventModel =
				SlackAnnounceEventModel.create(
						request.getEventId(),
						request.getTeamId(),
						request.getChannelId(),
						request.getUserId(),
						request.getMessageTs());

		SlackMessageParser.ParsedMessage parsed = slackMessageParser.parse(request.getText());

		SlackAnnounceEventModel savedEvent = slackAnnounceEventRepository.save(eventModel);

		AnnouncementModel announcementModel =
				AnnouncementModel.create(savedEvent.getId(), parsed.getTitle(), parsed.getBody());

		announcementRepository.save(announcementModel);
	}
}
