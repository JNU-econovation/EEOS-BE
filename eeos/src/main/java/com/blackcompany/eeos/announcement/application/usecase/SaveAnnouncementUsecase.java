package com.blackcompany.eeos.announcement.application.usecase;

import com.blackcompany.eeos.announcement.application.dto.SaveAnnouncementRequest;

public interface SaveAnnouncementUsecase {

	void save(SaveAnnouncementRequest request);
}
