package com.blackcompany.eeos.announcement.application.dto;

import com.blackcompany.eeos.announcement.application.model.AnnouncementModel;
import com.blackcompany.eeos.common.utils.DateConverter;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnnouncementResponse {

	private Long id;
	private String title;
	private String body;
	private long announcedAt;
	private Long deadline;
	private long createdDate;

	public static AnnouncementResponse from(AnnouncementModel model) {
		return AnnouncementResponse.builder()
				.id(model.getId())
				.title(model.getTitle())
				.body(model.getBody())
				.announcedAt(DateConverter.toMillis(model.getAnnouncedAt()))
				.deadline(DateConverter.toMillis(model.getDeadline()))
				.createdDate(DateConverter.toMillis(model.getCreatedDate()))
				.build();
	}
}
