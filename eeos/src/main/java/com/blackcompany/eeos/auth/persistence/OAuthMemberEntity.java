package com.blackcompany.eeos.auth.persistence;

import com.blackcompany.eeos.common.persistence.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = OAuthMemberEntity.ENTITY_PREFIX)
public class OAuthMemberEntity extends BaseEntity {
	public static final String ENTITY_PREFIX = "oauth_member";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = ENTITY_PREFIX + "_id", nullable = false)
	private Long id;

	@Column(name = ENTITY_PREFIX + "_oauth_id", nullable = false)
	@Builder.Default
	private String oauthId = "NONE";

	@Column(name = ENTITY_PREFIX + "_member_id", nullable = false)
	private Long memberId;
}
