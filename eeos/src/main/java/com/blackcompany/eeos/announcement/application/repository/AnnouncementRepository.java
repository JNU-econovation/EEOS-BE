package com.blackcompany.eeos.announcement.application.repository;

import com.blackcompany.eeos.announcement.application.model.AnnouncementModel;
import java.util.List;

public interface AnnouncementRepository {

	AnnouncementModel save(AnnouncementModel model);

	List<AnnouncementModel> findAllOrderByCreatedDateDesc();
}
