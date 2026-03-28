package com.blackcompany.eeos.announcement.persistence;

import com.blackcompany.eeos.announcement.application.model.SlackAnnounceEventModel;
import com.blackcompany.eeos.announcement.application.repository.SlackAnnounceEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SlackAnnounceEventRepositoryImpl implements SlackAnnounceEventRepository {

	private final JpaSlackAnnounceEventRepository jpaRepository;

	@Override
	public boolean existsByEventId(String eventId) {
		return jpaRepository.existsByEventId(eventId);
	}

	@Override
	public SlackAnnounceEventModel save(SlackAnnounceEventModel model) {
		SlackAnnounceEventEntity entity = SlackAnnounceEventEntity.toEntity(model);
		return jpaRepository.save(entity).toModel();
	}
}
