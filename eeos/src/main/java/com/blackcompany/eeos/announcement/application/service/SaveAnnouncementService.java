package com.blackcompany.eeos.announcement.application.service;

import com.blackcompany.eeos.announcement.application.dto.SaveAnnouncementRequest;
import com.blackcompany.eeos.announcement.application.model.AnnouncementModel;
import com.blackcompany.eeos.announcement.application.model.SlackAnnounceEventModel;
import com.blackcompany.eeos.announcement.application.repository.AnnouncementRepository;
import com.blackcompany.eeos.announcement.application.repository.SlackAnnounceEventRepository;
import com.blackcompany.eeos.announcement.application.support.AnnouncementParser;
import com.blackcompany.eeos.announcement.application.support.ParsedAnnouncement;
import com.blackcompany.eeos.announcement.application.usecase.SaveAnnouncementUsecase;
import com.blackcompany.eeos.common.utils.DateConverter;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SaveAnnouncementService implements SaveAnnouncementUsecase {

	private final SlackAnnounceEventRepository slackAnnounceEventRepository;
	private final AnnouncementRepository announcementRepository;
	private final AnnouncementParser announcementParser;

	public SaveAnnouncementService(
			SlackAnnounceEventRepository slackAnnounceEventRepository,
			AnnouncementRepository announcementRepository,
			@Qualifier("geminiAnnouncementParser") AnnouncementParser announcementParser) {
		this.slackAnnounceEventRepository = slackAnnounceEventRepository;
		this.announcementRepository = announcementRepository;
		this.announcementParser = announcementParser;
	}

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
		ParsedAnnouncement parsed = announcementParser.parse(request.getText());

		// messageTs -> LocalDateTime 변환
		LocalDateTime announcedAt =
				DateConverter.toLocalDateTime((long) (Double.parseDouble(request.getMessageTs()) * 1000));

		// Slack Event 저장
		SlackAnnounceEventModel savedEvent = slackAnnounceEventRepository.save(eventModel);

		// Announcement 생성
		AnnouncementModel announcementModel =
				AnnouncementModel.create(
						savedEvent.getId(), parsed.title(), parsed.body(), announcedAt, parsed.deadline());

		// Announcement 저장
		announcementRepository.save(announcementModel);
	}
}
