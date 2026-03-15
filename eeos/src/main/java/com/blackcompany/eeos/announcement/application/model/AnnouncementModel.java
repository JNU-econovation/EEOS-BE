package com.blackcompany.eeos.announcement.application.model;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnnouncementModel {

	private Long id;
	private Long slackAnnounceEventId;
	private String title;
	private String body;
	private LocalDateTime createdDate;

	public static AnnouncementModel create(Long slackAnnounceEventId, String title, String body) {
		return AnnouncementModel.builder()
				.slackAnnounceEventId(slackAnnounceEventId)
				.title(title)
				.body(body)
				.build();
	}
}
