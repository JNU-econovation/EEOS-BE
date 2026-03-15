package com.blackcompany.eeos.announcement.application.model;

import java.time.LocalDate;
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
	private LocalDateTime announcedAt;
	private LocalDate deadline;
	private LocalDateTime createdDate;

	public static AnnouncementModel create(
			Long slackAnnounceEventId,
			String title,
			String body,
			LocalDateTime announcedAt,
			LocalDate deadline) {
		return AnnouncementModel.builder()
				.slackAnnounceEventId(slackAnnounceEventId)
				.title(title)
				.body(body)
				.announcedAt(announcedAt)
				.deadline(deadline)
				.build();
	}
}
