package com.blackcompany.eeos.member.persistence;

import com.blackcompany.eeos.auth.application.domain.OauthServerType;
import com.blackcompany.eeos.common.persistence.BaseEntity;
import com.blackcompany.eeos.member.application.model.ActiveStatus;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

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
@Where(clause = "member_is_admin=false")
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
	private boolean isAdmin = false;
}
