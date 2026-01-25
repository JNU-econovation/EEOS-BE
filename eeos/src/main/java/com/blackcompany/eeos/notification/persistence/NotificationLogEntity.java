package com.blackcompany.eeos.notification.persistence;

import java.time.LocalDateTime;

import com.blackcompany.eeos.common.persistence.BaseEntity;
import com.blackcompany.eeos.notification.application.model.NotificationProvider;
import com.blackcompany.eeos.notification.application.model.NotificationStatus;
import com.blackcompany.eeos.program.persistence.ProgramCategory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@SuperBuilder(toBuilder = true)
@Table(name = NotificationLogEntity.ENTITY_PREFIX)
public class NotificationLogEntity extends BaseEntity {

	public static final String ENTITY_PREFIX = "NotificationLog";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = ENTITY_PREFIX + "_id", nullable = false)
	private Long id;

	@Column(name = ENTITY_PREFIX + "_program_id", nullable = false)
	private Long programId;

	@Column(name = ENTITY_PREFIX + "_push_token", nullable = false)
	private String pushToken;

	@Column(name = ENTITY_PREFIX + "_category", nullable = false)
	@Enumerated(EnumType.STRING)
	private ProgramCategory programCategory;

	@Column(name = ENTITY_PREFIX + "_title", nullable = false)
	private String messageTitle;

	@Column(name = ENTITY_PREFIX + "_body", nullable = false)
	private String messageBody;

	@Column(name = ENTITY_PREFIX + "_notification_status", nullable = false)
	@Enumerated(EnumType.STRING)
	private NotificationStatus status;

	@Column(name = ENTITY_PREFIX + "_error_code", nullable = true)
	private String errorCode;

	@Column(name = ENTITY_PREFIX + "_retry_count", nullable = false)
	private int retryCount;

	@Column(name = ENTITY_PREFIX + "_scheduled_at", nullable = false)
	private LocalDateTime scheduledAt; // 예정 발송 시간

	@Column(name = ENTITY_PREFIX + "_sent_at", nullable = true)
	private LocalDateTime sentAt;

	@Column(name = ENTITY_PREFIX + "_provider", nullable = false)
	@Enumerated(EnumType.STRING)
	private NotificationProvider provider;

}
