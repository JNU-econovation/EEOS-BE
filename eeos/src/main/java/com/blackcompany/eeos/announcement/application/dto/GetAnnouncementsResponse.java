package com.blackcompany.eeos.announcement.application.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetAnnouncementsResponse {

	private List<AnnouncementResponse> announcements;
}
