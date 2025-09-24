package com.blackcompany.eeos.member.persistence;

import com.blackcompany.eeos.auth.application.domain.OauthServerType;
import com.blackcompany.eeos.common.persistence.BaseEntity;
import com.blackcompany.eeos.member.application.model.ActiveStatus;
import com.blackcompany.eeos.member.application.model.Department;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@SuperBuilder(toBuilder = true)
@Entity
@Table(
		name = MemberEntity.ENTITY_PREFIX,
		indexes = {
			@Index(name = "idx_member_name", columnList = "member_name"),
			@Index(name = "idx_member_active_status", columnList = "member_active_status")
		})
public class MemberEntity extends BaseEntity {

	public static final String ENTITY_PREFIX = "member";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = ENTITY_PREFIX + "_id", nullable = false)
	private Long id;

	@Column(name = ENTITY_PREFIX + "_name", nullable = false)
	private String name;

	@Column(name = ENTITY_PREFIX + "_oath_server_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private OauthServerType oauthServerType;

	@Column(name = ENTITY_PREFIX + "_active_status", nullable = false)
	@Enumerated(EnumType.STRING)
	@Builder.Default
	private ActiveStatus activeStatus = ActiveStatus.AM;

	@Column(name = ENTITY_PREFIX + "_is_admin", nullable = false)
	@Builder.Default
	private boolean isAdmin = false; // TODO : 여러 ROEL 커버 가능하도록

	@Column(name = ENTITY_PREFIX + "_department", nullable = false)
	@Enumerated(EnumType.STRING)
	@Builder.Default
	private Department department = Department.NONE;
}
