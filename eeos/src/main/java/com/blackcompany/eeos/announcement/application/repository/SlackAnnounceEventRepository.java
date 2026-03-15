package com.blackcompany.eeos.announcement.application.repository;

import com.blackcompany.eeos.announcement.application.model.SlackAnnounceEventModel;

public interface SlackAnnounceEventRepository {

	boolean existsByEventId(String eventId);

	SlackAnnounceEventModel save(SlackAnnounceEventModel model);
}
