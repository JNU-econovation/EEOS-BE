package com.blackcompany.eeos.announcement.persistence;

import com.blackcompany.eeos.announcement.application.model.SlackAnnounceEventModel;
import com.blackcompany.eeos.common.persistence.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
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
		name = SlackAnnounceEventEntity.ENTITY_PREFIX,
		indexes = {@Index(name = "idx_slack_announce_event_created_date", columnList = "created_date")})
@SQLDelete(
		sql = "UPDATE slack_announce_event SET is_deleted = true WHERE slack_announce_event_id = ?")
@SQLRestriction("is_deleted=false")
public class SlackAnnounceEventEntity extends BaseEntity {

	public static final String ENTITY_PREFIX = "slack_announce_event";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = ENTITY_PREFIX + "_id", nullable = false)
	private Long id;

	@Column(name = ENTITY_PREFIX + "_event_id", nullable = false, unique = true)
	private String eventId;

	@Column(name = ENTITY_PREFIX + "_team_id", nullable = false, length = 50)
	private String teamId;

	@Column(name = ENTITY_PREFIX + "_channel_id", nullable = false, length = 50)
	private String channelId;

	@Column(name = ENTITY_PREFIX + "_user_id", nullable = false, length = 50)
	private String userId;

	@Column(name = ENTITY_PREFIX + "_message_ts", nullable = false, length = 50)
	private String messageTs;

	public SlackAnnounceEventModel toModel() {
		return SlackAnnounceEventModel.builder()
				.id(id)
				.eventId(eventId)
				.teamId(teamId)
				.channelId(channelId)
				.userId(userId)
				.messageTs(messageTs)
				.build();
	}

	public static SlackAnnounceEventEntity toEntity(SlackAnnounceEventModel model) {
		return SlackAnnounceEventEntity.builder()
				.id(model.getId())
				.eventId(model.getEventId())
				.teamId(model.getTeamId())
				.channelId(model.getChannelId())
				.userId(model.getUserId())
				.messageTs(model.getMessageTs())
				.build();
	}
}
