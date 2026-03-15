package com.blackcompany.eeos.announcement.application.dto;

import com.blackcompany.eeos.announcement.application.model.AnnouncementModel;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnnouncementResponse {

	private Long id;
	private String title;
	private String body;
	private LocalDateTime createdDate;

	public static AnnouncementResponse from(AnnouncementModel model) {
		return AnnouncementResponse.builder()
				.id(model.getId())
				.title(model.getTitle())
				.body(model.getBody())
				.createdDate(model.getCreatedDate())
				.build();
	}
}
