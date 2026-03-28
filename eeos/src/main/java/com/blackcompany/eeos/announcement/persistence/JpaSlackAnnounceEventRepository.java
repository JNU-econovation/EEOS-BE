package com.blackcompany.eeos.announcement.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaSlackAnnounceEventRepository
		extends JpaRepository<SlackAnnounceEventEntity, Long> {

	boolean existsByEventId(String eventId);
}
