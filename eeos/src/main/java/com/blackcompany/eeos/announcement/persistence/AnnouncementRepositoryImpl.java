package com.blackcompany.eeos.announcement.persistence;

import com.blackcompany.eeos.announcement.application.model.AnnouncementModel;
import com.blackcompany.eeos.announcement.application.repository.AnnouncementRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AnnouncementRepositoryImpl implements AnnouncementRepository {

	private final JpaAnnouncementRepository jpaRepository;

	@Override
	public AnnouncementModel save(AnnouncementModel model) {
		AnnouncementEntity entity = AnnouncementEntity.toEntity(model);
		return jpaRepository.save(entity).toModel();
	}

	@Override
	public List<AnnouncementModel> findAllOrderByCreatedDateDesc() {
		return jpaRepository.findAllByOrderByCreatedDateDesc().stream()
				.map(AnnouncementEntity::toModel)
				.toList();
	}
}
