package com.blackcompany.eeos.announcement.application.service;

import com.blackcompany.eeos.announcement.application.dto.AnnouncementResponse;
import com.blackcompany.eeos.announcement.application.dto.GetAnnouncementsResponse;
import com.blackcompany.eeos.announcement.application.repository.AnnouncementRepository;
import com.blackcompany.eeos.announcement.application.usecase.GetAnnouncementsUsecase;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetAnnouncementsService implements GetAnnouncementsUsecase {

	private final AnnouncementRepository announcementRepository;

	@Override
	@Transactional(readOnly = true)
	public GetAnnouncementsResponse getAnnouncements() {
		List<AnnouncementResponse> announcements =
				announcementRepository.findAllOrderByCreatedDateDesc().stream()
						.map(AnnouncementResponse::from)
						.toList();

		return GetAnnouncementsResponse.builder().announcements(announcements).build();
	}
}
