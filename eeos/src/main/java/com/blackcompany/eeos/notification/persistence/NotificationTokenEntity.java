package com.blackcompany.eeos.notification.persistence;

import com.blackcompany.eeos.common.persistence.BaseEntity;
import com.blackcompany.eeos.notification.application.model.NotificationPermission;
import com.blackcompany.eeos.notification.application.model.NotificationProvider;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
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
@Table(name = NotificationTokenEntity.ENTITY_PREFIX)
public class NotificationTokenEntity extends BaseEntity {

	public static final String ENTITY_PREFIX = "notification_token";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = ENTITY_PREFIX + "_id", nullable = false)
	private Long id;

	@Column(name = ENTITY_PREFIX + "_member_id", nullable = false)
	private Long memberId;

	@Column(name = ENTITY_PREFIX + "_provider", nullable = false, columnDefinition = "varchar(50)")
	@Enumerated(EnumType.STRING)
	private NotificationProvider provider;

	@Column(name = ENTITY_PREFIX + "_push_token", nullable = false, unique = true)
	private String pushToken;

	@Column(name = ENTITY_PREFIX + "_last_activate_at", nullable = false)
	private LocalDateTime lastActiveAt;

	@Column(
			name = ENTITY_PREFIX + "_notification_permission",
			nullable = false,
			columnDefinition = "varchar(50)")
	@Enumerated(EnumType.STRING)
	private NotificationPermission notificationPermission;
}
