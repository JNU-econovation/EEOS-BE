package com.blackcompany.eeos.announcement.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaAnnouncementRepository extends JpaRepository<AnnouncementEntity, Long> {

	List<AnnouncementEntity> findAllByOrderByCreatedDateDesc();
}
