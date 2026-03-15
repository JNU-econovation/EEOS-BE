package com.blackcompany.eeos.announcement.application.service;

import com.blackcompany.eeos.announcement.application.dto.SaveAnnouncementRequest;
import com.blackcompany.eeos.announcement.application.model.AnnouncementModel;
import com.blackcompany.eeos.announcement.application.model.SlackAnnounceEventModel;
import com.blackcompany.eeos.announcement.application.repository.AnnouncementRepository;
import com.blackcompany.eeos.announcement.application.repository.SlackAnnounceEventRepository;
import com.blackcompany.eeos.announcement.application.support.AnnouncementTextParser;
import com.blackcompany.eeos.announcement.application.usecase.SaveAnnouncementUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SaveAnnouncementService implements SaveAnnouncementUsecase {

	private final SlackAnnounceEventRepository slackAnnounceEventRepository;
	private final AnnouncementRepository announcementRepository;
	private final AnnouncementTextParser slackMessageParser;

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

		// Slack Event에 대한 정보 생성
		SlackAnnounceEventModel eventModel =
				SlackAnnounceEventModel.create(
						request.getEventId(),
						request.getTeamId(),
						request.getChannelId(),
						request.getUserId(),
						request.getMessageTs());

		// Slack 메세지 파싱
		AnnouncementTextParser.ParsedMessage parsed = slackMessageParser.parse(request.getText());

		// Slack Event 저장
		SlackAnnounceEventModel savedEvent = slackAnnounceEventRepository.save(eventModel);

		// Announcement 생성
		AnnouncementModel announcementModel =
				AnnouncementModel.create(savedEvent.getId(), parsed.getTitle(), parsed.getBody());

		// Announcement 저장
		announcementRepository.save(announcementModel);
	}
}
