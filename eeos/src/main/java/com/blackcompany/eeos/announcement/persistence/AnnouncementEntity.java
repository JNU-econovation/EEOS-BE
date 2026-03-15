package com.blackcompany.eeos.announcement.persistence;

import com.blackcompany.eeos.announcement.application.model.AnnouncementModel;
import com.blackcompany.eeos.common.persistence.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SuperBuilder(toBuilder = true)
@Entity
@Table(
		name = AnnouncementEntity.ENTITY_PREFIX,
		indexes = {@Index(name = "idx_announcement_created_date", columnList = "created_date")})
@SQLDelete(sql = "UPDATE announcement SET is_deleted = true WHERE announcement_id = ?")
@SQLRestriction("is_deleted=false")
public class AnnouncementEntity extends BaseEntity {

	public static final String ENTITY_PREFIX = "announcement";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = ENTITY_PREFIX + "_id", nullable = false)
	private Long id;

	@Column(name = ENTITY_PREFIX + "_slack_announce_event_id", nullable = false, unique = true)
	private Long slackAnnounceEventId;

	@Column(name = ENTITY_PREFIX + "_title", nullable = true)
	private String title;

	@Lob
	@Column(name = ENTITY_PREFIX + "_body", nullable = false, columnDefinition = "TEXT")
	private String body;

	public AnnouncementModel toModel() {
		Timestamp createdDate = getCreatedDate();
		return AnnouncementModel.builder()
				.id(id)
				.slackAnnounceEventId(slackAnnounceEventId)
				.title(title)
				.body(body)
				.createdDate(createdDate != null ? createdDate.toLocalDateTime() : null)
				.build();
	}

	public static AnnouncementEntity toEntity(AnnouncementModel model) {
		return AnnouncementEntity.builder()
				.id(model.getId())
				.slackAnnounceEventId(model.getSlackAnnounceEventId())
				.title(model.getTitle())
				.body(model.getBody())
				.build();
	}
}
